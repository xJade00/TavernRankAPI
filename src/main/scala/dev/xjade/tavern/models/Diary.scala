package dev.xjade.tavern.models

/**
 * Represents a specific Diary region
 */
enum Diary {
  case Ardougne, Desert, Falador, Fremennik, Kandarin, Karamja, Kourend,
  Lumbridge, Morytania, Varrock, WestProv, Wilderness
}

object Diary {
  implicit val orderingByName: Ordering[Diary] = Ordering.by(_.toString)

  def from(name: String): Diary = name match {
    case "Ardougne" => Ardougne
    case "Desert" => Desert
    case "Falador" => Falador
    case "Fremennik" => Fremennik
    case "Kandarin" => Kandarin
    case "Karamja" => Karamja
    case "Kourend" => Kourend
    case "Lumbridge" => Lumbridge
    case "Morytania" => Morytania
    case "Varrock" => Varrock
    case "WestProv" => WestProv
    case "Wilderness" => Wilderness
    case _ => throw new IllegalArgumentException(s"Unknown diary: $name")
  }
}