package dev.xjade.tavern.util

import dev.xjade.tavern.models.{CombatAchievementTier, DiaryTier}
import dev.xjade.tavern.models.db.{RankStructure, User}

object UserMerger {
  def merge(user1: User, user2: User): User = {
    User(
      username = user1.username, // Assuming usernames are the same or you want the first
      clogs = math.max(user1.clogs, user2.clogs),
      items = (user1.items.toSet ++ user2.items.toSet).toSeq, // Merging items into a Set to avoid duplicates
      ehp = math.max(user1.ehp, user2.ehp),
      ehb = math.max(user1.ehb, user2.ehb),
      level = math.max(user1.level, user2.level),
      diaries = mergeDiaries(user1.diaries, user2.diaries),
      combatAchievementTier = CombatAchievementTier.max(
        CombatAchievementTier.fromNone(user1.combatAchievementTier),
        CombatAchievementTier.fromNone(user2.combatAchievementTier)
      ).toString,
      lastKnownRank = mergeRank(user1.lastKnownRank, user2.lastKnownRank), // Assuming you have a method to merge RankStructure
      lastChecked = Seq(user1.lastChecked, user2.lastChecked).max, // Taking the most recent check
      wikiSyncEnabled = user1.wikiSyncEnabled, // Taking the first user's setting
      collectionLogEnabled = user1.collectionLogEnabled // Taking the first user's setting
    )
  }

  private def mergeDiaries(diaries1: Map[String, String], diaries2: Map[String, String]): Map[String, String] = {
    diaries1.map { case (diary, tier1) =>
      val tier2 = diaries2(diary)
      val maxTier = DiaryTier.max(DiaryTier.from(tier1), DiaryTier.from(tier2))
      diary -> maxTier.toString
    }
  }


  private def mergeRank(rank1: RankStructure, rank2: RankStructure): RankStructure = {
    // Assuming RankStructure has fields that can be compared, and we take the greater rank.
    if(rank2.name == "UNKNOWN") rank1
    else rank2
  }
}
