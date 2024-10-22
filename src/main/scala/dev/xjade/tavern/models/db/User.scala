package dev.xjade.tavern.models.db

import dev.xjade.tavern.models.{CombatAchievementTier, Diary}
import play.api.libs.json.{Format, Json}

import java.time.Instant
import java.util.Date

import dev.xjade.tavern.models.db.RankStructure._

case class User(
               username: String,
               clogs: Int,
               items: Seq[String],
               ehp: Long,
               ehb: Long,
               level: Int,
               diaries: Map[String, String],
               combatAchievementTier: String,
               lastKnownRank: RankStructure,
               lastChecked: Date,
               wikiSyncEnabled: Boolean,
               collectionLogEnabled: Boolean
               )

object User {
  def defaultUser(
                   username: String,
                   clogs: Int = 0,
                   items: Seq[String] = Nil,
                   ehp: Long = 0,
                   ehb: Long = 0,
                   level: Int = 0,
                   diaries: Map[String, String] = Diary.values.map(diary => diary.toString -> "None").toMap,
                   combatAchievementTier: String = CombatAchievementTier.None.toString,
                   lastKnownRank: RankStructure = RankStructure("UNKNOWN", "UNKNOWN"),
                   lastChecked: Date = Date.from(Instant.now()),
                   wikiSyncEnabled: Boolean = false,
                   collectionLogEnabled: Boolean = false
                 ): User = User(
    username = username,
    clogs = clogs,
    items = items,
    ehp = ehp,
    ehb = ehb,
    level = level,
    diaries = diaries,
    combatAchievementTier = combatAchievementTier,
    lastKnownRank = lastKnownRank,
    lastChecked = lastChecked,
    wikiSyncEnabled = wikiSyncEnabled,
    collectionLogEnabled = collectionLogEnabled
  )
  
  implicit val format: Format[User] = Json.format[User]
}
