package dev.xjade.tavern.models.api.clog

import play.api.libs.json.{Format, Json}

case class CollectionLog(
                          tabs: Map[String, Map[String, BossInfo]],
                          username: String,
                          accountType: String,
                          totalObtained: Int,
                          totalItems: Int,
                          uniqueObtained: Int,
                          uniqueItems: Int
                        )

object CollectionLog {
  implicit val format: Format[CollectionLog] = Json.format[CollectionLog]
}