package com.nudemeth.example.web.controller

import org.scalatra._

class PageController extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
