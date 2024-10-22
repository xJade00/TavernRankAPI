package dev.xjade.tavern

import dev.xjade.tavern.db.DbWrapper
import dev.xjade.tavern.requests.{DataCollector, UserHandler}
import io.javalin.http.{Context, Handler}
import dev.xjade.tavern.util.JavalinImplicits.CtxImplicits


object ApiRoutes {

  val getUser: Handler = { ctx =>
    val name = ctx.pathParam("name")
    val data = UserHandler.getUserInformation(name)
    ctx.sjson(data)
  }

  val getItems: Handler = { ctx =>
    val items = DbWrapper.getItems()
    ctx.sjson(items)
  }

  val patchItems: Handler = { ctx =>
    val body = ctx.body()
    
    DbWrapper.saveItems(body)
  }
}

