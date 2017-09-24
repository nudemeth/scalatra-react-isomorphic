package com.nudemeth.example.web

import javax.servlet.ServletContext

import com.nudemeth.example.web.controller.PageController
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new PageController, "/*")
  }
}
