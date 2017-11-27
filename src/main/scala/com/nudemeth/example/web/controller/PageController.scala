package com.nudemeth.example.web.controller

import akka.actor.ActorSystem
import com.nudemeth.example.web.viewmodel._
import org.json4s.jackson.Serialization.write
import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.{ExecutionContext, Future}

class PageController(system: ActorSystem) extends BaseController with FutureSupport {

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/") {
    new AsyncResult() {
      override val is = Future {
        val model = write(new HomeViewModel(s"This is Home page"))
        val content = renderer.invokeMethod[String]("frontend", "renderServer", requestPath, model)
        views.html.index.render(content, model)
      }
    }
  }

  get("/about") {
    new AsyncResult {
      override val is = Future {
        val model = write(new AboutViewModel(s"About page"))
        val content = renderer.invokeMethod[String]("frontend", "renderServer", requestPath, model)
        views.html.index.render(content, model)
      }
    }
  }
}