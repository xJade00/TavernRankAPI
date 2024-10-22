package dev.xjade.tavern.models.api.wiki

import dev.xjade.tavern.models.api.wiki.RawWikiSyncUser.tiers
import dev.xjade.tavern.models.{Diary, DiaryTier}
import dev.xjade.tavern.models.Diary.*

case class RawWikiSyncUser(
                            username: String,
                            timestamp: String,
                            quests: Map[String, Int],
                            achievement_diaries: AchievementDiaries,
                            levels: Map[String, Int],
                            music_tracks: Map[String, Boolean],
                            combat_achievements: List[Int]
                          ) {
  val diaries: Map[Diary, DiaryTier] = Map(
    Ardougne -> achievement_diaries.Ardougne,
    Desert -> achievement_diaries.Desert,
    Falador -> achievement_diaries.Falador,
    Fremennik -> achievement_diaries.Fremennik,
    Kandarin -> achievement_diaries.Kandarin,
    Karamja -> achievement_diaries.Karamja,
    Kourend -> achievement_diaries.KourendKebos,
    Lumbridge -> achievement_diaries.LumbridgeDraynor,
    Morytania -> achievement_diaries.Morytania,
    Varrock -> achievement_diaries.Varrock,
    WestProv -> achievement_diaries.WesternProvinces,
    Wilderness -> achievement_diaries.Wilderness
  ).map { (location, tasks) =>
    (location, DiaryTier.fromOrdinal(tiers.map(tier => tasks(tier).complete)
      .zipWithIndex
      .find { case (complete, _) => !complete }
      .map { case (_, idx) => idx }
      .getOrElse(4)))
  }
}

case object RawWikiSyncUser {
  val tiers = List("Easy", "Medium", "Hard", "Elite")
}