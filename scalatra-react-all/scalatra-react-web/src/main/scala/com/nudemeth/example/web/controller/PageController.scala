package com.nudemeth.example.web.controller

import org.scalatra._

class PageController extends ScalatraServlet {

  get("/") {
    val content = "<div><h1>Hello World #1<h1><div>"
    val data = "{\"greeting\": \"Hello World #2\"}"
    views.html.index.render(content, data)
  }

}
