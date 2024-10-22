package dev.xjade.tavern.util

import play.api.libs.json.*
import requests.*
import dev.xjade.tavern.models.CombatAchievementTier.{None, Tier}

object CombatAchievementParser {

  def getIds(tier: Tier): List[Int] = {
    if (tier == None) return List()
    val json = fetchJson(tier)
    extractIds(json)
  }

  private def fetchJson(tier: Tier): JsValue = Json.parse(requests.get(s"https://oldschool.runescape.wiki/w/Special:Ask/limit%3D500/format%3Djson/order%3Dasc/sort%3D/offset%3D0/-5B-5BCategory:$tier-20Combat-20Achievements-20tasks-5D-5D/-3FCombat-20Achievement-20JSON/mainlabel%3D/prettyprint%3Dtrue/unescape%3Dtrue/searchlabel%3DJSON").text())

  // Function to extract a list of IDs from the JSON data
  private def extractIds(json: JsValue): List[Int] = extractIdsWithNames(json).map(_._1)

  private def extractIdsWithNames(json: JsValue): List[(Int, String)] = {
    (json \ "results").as[JsObject].fields.flatMap {
      case (_, result) =>
        val name = (result \ "fulltext").as[String]
        (result \ "printouts" \ "Combat Achievement JSON").as[JsArray].value.map { entry =>
          val id = (Json.parse(entry.as[String]) \ "id").as[String].toInt
          (id, name)
        }
    }.toList
  }
}
