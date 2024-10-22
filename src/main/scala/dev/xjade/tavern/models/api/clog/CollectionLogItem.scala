package dev.xjade.tavern.models.api.clog

import play.api.libs.json.{Format, Json}

case class CollectionLogItem(
                              id: Int,
                              name: String,
                              quantity: Int,
                              obtained: Boolean,
                              obtainedAt: Option[String],
                              sequence: Int
                            )

object CollectionLogItem {
  implicit val format: Format[CollectionLogItem] = Json.format[CollectionLogItem]
}