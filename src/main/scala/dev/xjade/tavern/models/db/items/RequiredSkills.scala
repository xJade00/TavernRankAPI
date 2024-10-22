package dev.xjade.tavern.models.db.items

import play.api.libs.json.{Format, Json}

case class RequiredSkills(
                        name: String,
                        level: Int
                        )

object RequiredSkills {
 implicit val format: Format[RequiredSkills] = Json.format[RequiredSkills]
}
