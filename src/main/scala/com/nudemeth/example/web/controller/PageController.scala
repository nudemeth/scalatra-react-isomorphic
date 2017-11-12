package com.nudemeth.example.web.controller

import com.nudemeth.example.web.viewmodel._
import org.json4s.jackson.Serialization.write

class PageController extends BaseController {

  get("/") {
    val model = write(HomeViewModel(s"This is Home page"))
    val content = nashorn.invokeMethod[String]("frontend", "renderServer", "/", model)
    views.html.index.render(content, model)
  }

  get("/about") {
    val model = write(AboutViewModel(s"About page"))
    val content = nashorn.invokeMethod[String]("frontend", "renderServer", "/about", model)
    views.html.index.render(content, model)
  }
}
