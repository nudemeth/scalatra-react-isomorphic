package com.nudemeth.example.web.controller

import akka.actor.ActorSystem
import com.nudemeth.example.web.viewmodel._
import org.json4s.jackson.Serialization.write
import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.{ExecutionContext, Future}

class DataController(system: ActorSystem) extends BaseController with FutureSupport {

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/home") {
    new AsyncResult() {
      override val is = Future {
        contentType = "application/json"
        write(new HomeViewModel(s"This is Home page"))
      }
    }
  }

  get("/about") {
    new AsyncResult() {
      override val is = Future {
        contentType = "application/json"
        write(new AboutViewModel(s"About page"))
      }
    }
  }

}
