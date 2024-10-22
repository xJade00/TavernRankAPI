package dev.xjade.tavern.models.api.wiki

case class AchievementDiaries(
                               Ardougne: Map[String, TaskCompletion],
                               Desert: Map[String, TaskCompletion],
                               Falador: Map[String, TaskCompletion],
                               Fremennik: Map[String, TaskCompletion],
                               Kandarin: Map[String, TaskCompletion],
                               Karamja: Map[String, TaskCompletion],
                               KourendKebos: Map[String, TaskCompletion],
                               LumbridgeDraynor: Map[String, TaskCompletion],
                               Morytania: Map[String, TaskCompletion],
                               Varrock: Map[String, TaskCompletion],
                               WesternProvinces: Map[String, TaskCompletion],
                               Wilderness: Map[String, TaskCompletion]
                             )