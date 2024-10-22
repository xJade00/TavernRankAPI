package dev.xjade.tavern.db

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcType
import slick.ast.BaseTypedType
import play.api.libs.json._

object CustomColumnTypes {
  // Custom column mapping for Seq[String]
  implicit val seqStringColumnType: JdbcType[Seq[String]] with BaseTypedType[Seq[String]] =
    MappedColumnType.base[Seq[String], String](
      seq => Json.toJson(seq).toString(), // Convert Seq[String] to JSON string
      str => Json.parse(str).as[Seq[String]] // Convert JSON string to Seq[String]
    )

  // Custom column mapping for Map[String, String]
  implicit val mapStringColumnType: JdbcType[Map[String, String]] with BaseTypedType[Map[String, String]] =
    MappedColumnType.base[Map[String, String], String](
      map => Json.toJson(map).toString(), // Convert Map[String, String] to JSON string
      str => Json.parse(str).as[Map[String, String]] // Convert JSON string to Map[String, String]
    )
}