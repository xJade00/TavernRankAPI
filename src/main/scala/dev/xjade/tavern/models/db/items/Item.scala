package dev.xjade.tavern.models.db.items

import play.api.libs.json.{Format, Json}

import dev.xjade.tavern.models.db.items.RequiredItems._
import dev.xjade.tavern.models.db.items.RequiredSkills._

case class Item(
               clog: String = "",
               name: String,
               requiredItems: Seq[RequiredItems] = Nil,
               requiredSkills: Seq[RequiredSkills] = Nil,
               image: String,
               quest: String = "",
               combatAchievement: Int = -1,
               )

object Item {
   implicit val format: Format[Item] = Json.format[Item]
}
