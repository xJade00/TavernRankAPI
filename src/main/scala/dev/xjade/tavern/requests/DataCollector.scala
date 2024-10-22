package dev.xjade.tavern.requests

import dev.xjade.tavern.models.CombatAchievementTier
import dev.xjade.tavern.models.api.Temple
import dev.xjade.tavern.models.api.clog.CollectionLogData
import dev.xjade.tavern.models.api.wiki.{AchievementDiaries, RawWikiSyncUser, TaskCompletion}
import play.api.libs.json.{Json, Reads}
import requests.Response
import play.api.libs.functional.syntax.*
import play.api.libs.json.*

import scala.util.Try

object DataCollector {

  val pointsMap: Map[Int, Int] = CombatAchievementTier.Ordinals
    .flatMap(tier => tier.ids.map(id => id -> tier.pointsPer))
    .toMap ++ Map(-1 -> 0)

  def getTempleData(name: String): Temple = {
    val r: Response = requests.get(s"https://templeosrs.com/api/player_stats.php?player=$name&bosses=1")
    val body = r.text()
    Json.parse(body).as[Temple]
  }

  def getClogData(name: String): (Int, Seq[String], Boolean) =
    Try {
      val r: Response = requests.get(s"https://api.collectionlog.net/collectionlog/user/$name")
      val body = r.text()

      val raw = Json.parse(body).as[CollectionLogData]
      val obtainedItemNames = raw.collectionLog.tabs.flatMap { case (_, tabs) =>
        tabs.flatMap { case (_, items) =>
          items.items.filter(_.obtained).map(_.name)
        }
      }.toSeq

      (raw.collectionLog.uniqueObtained, obtainedItemNames, true)
    }.getOrElse((-1, Nil, false))

  def getUserWikiData(name: String): (RawWikiSyncUser, Boolean) =
    Try {
      val r: Response = requests.get(s"https://sync.runescape.wiki/runelite/player/$name/STANDARD")
      val body = r.text()

      implicit val taskCompletionReads: Reads[TaskCompletion] = Json.reads[TaskCompletion]

      implicit val achievementDiariesReads: Reads[AchievementDiaries] = (
        (__ \ "Ardougne").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Desert").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Falador").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Fremennik").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Kandarin").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Karamja").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Kourend & Kebos").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Lumbridge & Draynor").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Morytania").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Varrock").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Western Provinces").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty)) and
          (__ \ "Wilderness").read[Map[String, TaskCompletion]].orElse(Reads.pure(Map.empty))
        )(AchievementDiaries.apply _)

      implicit val rawWikiSyncUserReads: Reads[RawWikiSyncUser] = Json.reads[RawWikiSyncUser]

      val raw = Json.parse(body).as[RawWikiSyncUser]
      (RawWikiSyncUser(
        username = raw.username,
        timestamp = raw.timestamp,
        quests = raw.quests ++ Map("" -> 2),
        achievement_diaries = raw.achievement_diaries,
        levels = raw.levels,
        music_tracks = raw.music_tracks,
        combat_achievements = raw.combat_achievements :+ -1
      ), true)
    }.getOrElse((null, false))

}
