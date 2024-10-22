package dev.xjade.tavern

import io.javalin.Javalin


object Main {
  def main(args: Array[String]): Unit = {
    val app = Javalin.create()
      .get("/users/<name>", ApiRoutes.getUser)
      .get("/items", ApiRoutes.getItems)
      .patch("/items", ApiRoutes.patchItems)
      .before { ctx => println(ctx.url()) }
      .after { ctx => println("aftER?")}
      .start("167.99.233.205", 8080)
  }
}
