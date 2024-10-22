package dev.xjade.tavern.models

import dev.xjade.tavern.models.db.{RankStructure, User}
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.{ProvenShape, Tag}

class Users(tag: Tag) extends Table[User](tag, "users") {
  import dev.xjade.tavern.models.db.Implicits.*

  def username: Rep[String] = column[String]("username", O.PrimaryKey)
  def clogs: Rep[Int] = column[Int]("clogs")
  def items: Rep[Seq[String]] = column[Seq[String]]("items")(seqStringColumnType)
  def ehp: Rep[Long] = column[Long]("ehp")
  def ehb: Rep[Long] = column[Long]("ehb")
  def level: Rep[Int] = column[Int]("level")
  def diaries: Rep[Map[String, String]] = column[Map[String, String]]("diaries")(mapStringStringColumnType)
  def combatAchievementTier: Rep[String] = column[String]("combat_achievement_tier")
  def lastKnownRank: Rep[RankStructure] = column[RankStructure]("last_known_rank")(RankStructure.rankStructureColumnType)
  def lastChecked: Rep[java.util.Date] = column[java.util.Date]("last_checked")(utilDateColumnType)  // Use custom MappedColumnType for java.util.Date
  def wikiSyncEnabled: Rep[Boolean] = column[Boolean]("wiki_sync_enabled")
  def collectionLogEnabled: Rep[Boolean] = column[Boolean]("collection_log_enabled")

  override def * = (
    username,
    clogs,
    items,
    ehp,
    ehb,
    level,
    diaries,
    combatAchievementTier,
    lastKnownRank,
    lastChecked,
    wikiSyncEnabled,
    collectionLogEnabled
  ) <> ((User.apply _).tupled, User.unapply)
}