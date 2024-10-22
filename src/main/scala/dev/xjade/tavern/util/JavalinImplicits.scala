package dev.xjade.tavern.util

import io.javalin.http.Context
import play.api.libs.json.*

object JavalinImplicits {
  implicit class CtxImplicits(ctx: Context) {
    def sjson[T](any: T)(implicit writes: Writes[T]): Unit = {
      val json = Json.toJson(any).toString
      ctx.contentType("application/json"); // Set the content type to JSON
      ctx.result(json);
    }
  }
}
