package com.nudemeth.example.web

import javax.servlet.ServletContext

import com.nudemeth.example.web.controller.{DataController, PageController}
import org.scalatra._
import _root_.akka.actor.ActorSystem

class ScalatraBootstrap extends LifeCycle {
  val system = ActorSystem()

  override def init(context: ServletContext) {
    context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")
    context.mount(new PageController(system), "/*")
    context.mount(new DataController(system), "/data/*")
  }

  override def destroy(context:ServletContext) {
    system.terminate()
  }
}
