package com.nudemeth.example.web.controller

import com.nudemeth.example.web.viewmodel._
import org.json4s.jackson.Serialization.write

class PageController extends BaseController {

  get("/") {
    val model = write(new HomeViewModel(s"This is Home page"))
    val content = renderer.invokeMethod[String]("frontend", "renderServer", requestPath, model)
    views.html.index.render(content, model)
  }

  get("/about") {
    val model = write(new AboutViewModel(s"About page"))
    val content = renderer.invokeMethod[String]("frontend", "renderServer", requestPath, model)
    views.html.index.render(content, model)
  }
}
