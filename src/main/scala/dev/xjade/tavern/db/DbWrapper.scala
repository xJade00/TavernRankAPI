package dev.xjade.tavern.db

import better.files.File
import dev.xjade.tavern.models.Users
import dev.xjade.tavern.models.db.User
import dev.xjade.tavern.models.db.items.Item
import play.api.libs.json.{JsResultException, Json}
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DbWrapper {
  private val db = Database.forConfig("postgres")
  
  def getItems(): Seq[Item] = {
    val file = File("./items.json")
    if (file.notExists) {
      file.createFileIfNotExists()
      file.write("[]")
    }

    val jsonContent = file.contentAsString
    val items = Json.parse(jsonContent).as[Seq[Item]]

    items
  }

  def saveItems(jsonString: String): Boolean = {
    try {
      val items = Json.parse(jsonString).as[Seq[Item]]
      val file = File("./items.json")
      file.write(Json.toJson(items).toString())
      true
    } catch {
      case _: JsResultException => false
    }
  }

  private val users = TableQuery[Users]

  def getUser(username: String): Option[User] = {
    val query = users.filter(_.username === username).result.headOption
    val userFuture = db.run(query)
    Await.result(userFuture, Duration.Inf)
  }

  def saveUser(user: User): Unit = {
    val query = users.insertOrUpdate(user)
    val saveFuture = db.run(query)
    Await.result(saveFuture, Duration.Inf)
  }
}
