package dev.xjade.tavern.models.api

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Temple(
                   ehp: Float,
                   ehb: Float,
                   level: Int,
                   username: String
                 )

object Temple {
  implicit val templeReads: Reads[Temple] = (
    (JsPath \ "data" \ "Overall_ehp").read[Float] and
      (JsPath \ "data" \ "Im_ehb").read[Float] and
      (JsPath \ "data" \ "Overall_level").read[Int] and
      (JsPath \ "data" \ "info" \ "Username").read[String]
    )(Temple.apply _)
}
