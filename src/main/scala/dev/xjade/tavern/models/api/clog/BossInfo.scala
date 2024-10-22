package dev.xjade.tavern.models.api.clog

import play.api.libs.json.{Format, Json, Reads}

import scala.collection.immutable.Seq

case class BossInfo(
                     items: Seq[CollectionLogItem]
                   )

object BossInfo {
  implicit val bossInfoReads: Format[BossInfo] = Json.format[BossInfo]
}
