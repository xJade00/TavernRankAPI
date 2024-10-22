package dev.xjade.tavern.models.db.items

import play.api.libs.json.{Format, Json}

case class RequiredItems(
                        name: String,
                        amount: Int
                        )

object RequiredItems {
  implicit val format: Format[RequiredItems] = Json.format[RequiredItems]
}
