package dev.xjade.tavern.requests

import dev.xjade.tavern.db.DbWrapper
import dev.xjade.tavern.models.{CombatAchievementTier, Diary, DiaryTier}
import dev.xjade.tavern.models.CombatAchievementTier.Elite
import dev.xjade.tavern.models.api.wiki.RawWikiSyncUser
import dev.xjade.tavern.models.db.User
import dev.xjade.tavern.util.UserMerger

import java.time.Instant
import java.util.Date

object UserHandler {
  val miniquests = List("Skippy and the Mogres", "Mage Arena II", "Mage Arena I", "Lair of Tarn Razorlor", "Into the Tombs", "In Search of Knowledge", "Hopespear's Will", "His Faithful Servants", "The General's Shadow", "The Frozen Door", "Family Pest", "Enter the Abyss", "The Enchanted Key", "Daddy's Home", "Curse of the Empty Lord", "Bear Your Soul", "Barbarian Training", "Alfred Grimhand's Barcrawl")

  val holidayTracks = List("Bunny's Sugar Rush", "Diango's Little Helpers", "Dies Irae", "Dot's Yuletide", "Easter Jig", "Eve's Epinette", "A Festive Party", "Funny Bunnies", "Grimly Fiendish", "High Spirits", "Jungle Bells", "Jungle Island Xmas", "Land of Snow", "Nox Irae", "Scrubfoot's Descent", "Sea Minor Shanty", "Sea Shanty Xmas", "Sunny Side Up", "Winter Funfair")

  private case class DataPasser(user: User, hasWikiSync: Boolean, wiki: RawWikiSyncUser, capes: Seq[String])

  def getUserInformation(name: String): User = {
    val temple = DataCollector.getTempleData(name)
    
    val username = temple.username

    // Special capes
    val maxCape = if (temple.level == 2277) List("Max Cape") else Nil

    val passer = getWikiSyncData(username, DbWrapper.getUser(username))

    val found = getClogInfo(passer, maxCape)

    UserMerger.merge(found, User.defaultUser(
      username = username,
      level = temple.level,
      ehp = Math.round(temple.ehp),
      ehb = Math.round(temple.ehb)
    ))
  }

  private def getClogInfo(data: DataPasser, maxCape: Seq[String]): User = {
    val (amountObtained, obtainedItems, hasClogPlugin) = DataCollector.getClogData(data.user.username)

    if (!hasClogPlugin) return data.user

    val obtained = obtainedItems ++ maxCape
    val obtainedItemCounts = obtainedItems.groupBy(identity).view.mapValues(_.size).toMap

    val importantItems = DbWrapper.getItems()
    println("Important items: " + importantItems)
    val filteredItems = importantItems.collect {
      case item if data.wiki.combat_achievements.contains(item.combatAchievement) &&
        data.wiki.quests.getOrElse(item.quest, 0) == 2 &&
        item.requiredSkills.forall(skill => data.wiki.levels.getOrElse(skill.name, 0) >= skill.level) &&
        item.requiredItems.forall(reqItem => obtainedItemCounts.getOrElse(reqItem.name, 0) >= reqItem.amount)
      => item.name
    }

    val items = filteredItems ++ maxCape ++ data.capes

    val made = User.defaultUser(
      username = data.user.username,
      clogs = amountObtained,
      items = items,
      wikiSyncEnabled = data.hasWikiSync,
      collectionLogEnabled = true)

    UserMerger.merge(made, data.user)
  }

  private def getWikiSyncData(username: String, user: Option[User]): DataPasser = {
    val (wiki, hasWikiPlugin) = DataCollector.getUserWikiData(username)

    if (!hasWikiPlugin) {
      return user match {
        case Some(value) => DataPasser(value, false, null, Nil)
        case None => DataPasser(User.defaultUser(username), false, null, Nil)
      }
    }

    val found = user.getOrElse(User.defaultUser(username))

    val qpc = if (wiki.quests.filter { case (name, _) => !miniquests.contains(name) }.forall { case (_, state) => state == 2 }) Seq("Quest Cape") else Nil
    val mc = if (wiki.music_tracks.filter { case (name, _) => !holidayTracks.contains(name) }.forall { case (_, obtained) => obtained }) Seq("Music Cape") else Nil


    val diaries = wiki.diaries.map { case (diary, tier) =>
      val userTier = DiaryTier.from(found.diaries(diary.toString))
      val max = DiaryTier.max(tier, userTier)

      (diary.toString, max.toString)
    }

    val tier = CombatAchievementTier.Ordinals.findLast { x =>
      val userPoints = wiki.combat_achievements.map(DataCollector.pointsMap).sum
      userPoints >= x.pointsRequired
    }.getOrElse(CombatAchievementTier.None)

    val made = User.defaultUser(
      username,
      combatAchievementTier = CombatAchievementTier.max(tier, CombatAchievementTier.fromNone(found.combatAchievementTier)).toString,
      diaries = diaries, wikiSyncEnabled = true)

    DataPasser(user = UserMerger.merge(made, found), true, wiki, qpc ++ mc)
  }
}
