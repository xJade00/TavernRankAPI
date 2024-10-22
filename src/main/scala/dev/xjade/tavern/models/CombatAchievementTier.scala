package dev.xjade.tavern.models

import dev.xjade.tavern.util.CombatAchievementParser.*

object CombatAchievementTier {
  val Ordinals: Seq[Tier] = Seq(None, Easy, Medium, Hard, Elite, Master, Grandmaster)
  sealed class Tier(val pointsRequired: Int) {
    lazy val ids: List[Int] = getIds(this)
    lazy val pointsPer: Int = Ordinals.indexOf(this)
  }

  case object None extends Tier(0)
  case object Easy extends Tier(35)
  case object Medium extends Tier(131)
  case object Hard extends Tier(341)
  case object Elite extends Tier(925)
  case object Master extends Tier(1675)
  case object Grandmaster extends Tier(2317)

  def max(tier1: Tier, tier2: Tier): Tier = if (tier1.pointsRequired >= tier2.pointsRequired) tier1 else tier2
  def from(name: String) = Ordinals.find(_.toString == name)
  def fromNone(name: String) = from(name).getOrElse(None)
}
