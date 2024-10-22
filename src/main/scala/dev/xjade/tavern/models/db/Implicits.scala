package dev.xjade.tavern.models.db

import dev.xjade.tavern.models.db.items.Item
import play.api.libs.json.*
import slick.jdbc.PostgresProfile.api.*
import dev.xjade.tavern.models.db.items.Item.format

object Implicits {

  implicit val anyReads: Reads[Any] = Reads {
    case js: JsString => JsSuccess(js.value)
    case js: JsNumber => JsSuccess(js.value)
    case js: JsBoolean => JsSuccess(js.value)
    case js: JsObject => JsSuccess(js.value)
    case js: JsArray => JsSuccess(js.value)
    case JsNull => JsSuccess(null)
    case _ => JsError("Unknown type")
  }

  implicit val mapReads: Reads[Map[String, Any]] = Reads[Map[String, Any]] {
    case JsObject(fields) =>
      JsSuccess(fields.map {
        case (key, value) => key -> anyReads.reads(value).get
      }.toMap)
    case _ => JsError("Expected JsObject")
  }
  
  // Custom mapping for Map[String, String] to JSONB
  implicit val mapStringStringColumnType: BaseColumnType[Map[String, String]] =
    MappedColumnType.base[Map[String, String], String](
      map => Json.toJson(map).toString(), // Convert Map to JSON string
      jsonString => Json.parse(jsonString).as[Map[String, String]] // Convert JSON string to Map
    )

  // Custom mapping for Seq[String] to JSONB
  implicit val seqStringColumnType: BaseColumnType[Seq[String]] =
    MappedColumnType.base[Seq[String], String](
      seq => Json.toJson(seq).toString(), // Convert Seq[String] to JSON string
      jsonString => Json.parse(jsonString).as[Seq[String]] // Convert JSON string to Seq[String]
    )

  implicit val utilDateColumnType: BaseColumnType[java.util.Date] =
    MappedColumnType.base[java.util.Date, java.sql.Date](
      utilDate => new java.sql.Date(utilDate.getTime), // Convert java.util.Date to java.sql.Date
      sqlDate => new java.util.Date(sqlDate.getTime) // Convert java.sql.Date to java.util.Date
    )

}