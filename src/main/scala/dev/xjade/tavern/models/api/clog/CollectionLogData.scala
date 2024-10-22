package dev.xjade.tavern.models.api.clog

import play.api.libs.json.{Format, Json, Reads, Writes}

case class CollectionLogData(
                              collectionLogId: String,
                              userId: String,
                              collectionLog: CollectionLog
                            )

object CollectionLogData {
  implicit val reads: Reads[CollectionLogData] = Json.reads[CollectionLogData]
  implicit val writes: Writes[CollectionLogData] = Json.writes[CollectionLogData]
}