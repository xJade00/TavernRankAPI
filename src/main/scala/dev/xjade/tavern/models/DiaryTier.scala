package dev.xjade.tavern.models

/**
 * Represents a Tier of Diary. Should always be the highest the user has done (including lowers, you need Easy to have Medium)
 */
enum DiaryTier {
  case None, Easy, Medium, Hard, Elite
}
object DiaryTier {
  implicit val orderingByName: Ordering[DiaryTier] = Ordering.by(_.toString)

  def from(name: String): DiaryTier = name match {
    case "None" => None
    case "Easy" => Easy
    case "Medium" => Medium
    case "Hard" => Hard
    case "Elite" => Elite
    case _ => throw new IllegalArgumentException(s"Unknown diary tier: $name")
  }

  def max(tier1: DiaryTier, tier2: DiaryTier): DiaryTier =
    if (tier1.ordinal >= tier2.ordinal) tier1 else tier2
}