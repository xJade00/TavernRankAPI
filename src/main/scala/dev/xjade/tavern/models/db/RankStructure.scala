package dev.xjade.tavern.models.db

import play.api.libs.json.{Format, Json}
import slick.jdbc.PostgresProfile.api._
import play.api.libs.json._
import slick.sql.SqlProfile.ColumnOption.SqlType

case class RankStructure(
                        structure: String,
                        name: String
                        )

object RankStructure {
  implicit val format: Format[RankStructure] = Json.format[RankStructure]

  // Conversion between RankStructure and JSON strings for Slick
  implicit val rankStructureColumnType: BaseColumnType[RankStructure] = MappedColumnType.base[RankStructure, String](
    rankStructure => Json.toJson(rankStructure).toString(), // Convert RankStructure to JSON string
    jsonString => Json.parse(jsonString).as[RankStructure] // Convert JSON string to RankStructure
  )
}
