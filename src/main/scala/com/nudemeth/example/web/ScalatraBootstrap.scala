package com.nudemeth.example.web

import javax.servlet.ServletContext

import com.nudemeth.example.web.controller.{DataController, PageController}
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")
    context.mount(new PageController, "/*")
    context.mount(new DataController, "/data/*")
  }
}
