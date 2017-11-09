package com.nudemeth.example.web.controller

import com.nudemeth.example.web.engine._
import com.nudemeth.example.web.viewmodel.HomeViewModel
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.json4s.jackson.Serialization.write

class PageController extends ScalatraServlet {

  implicit val jsonFormats: Formats = DefaultFormats

  private lazy val nashorn: JavaScriptEngine = new NashornEngine(
    Seq(
      ScriptURL(getClass.getResource("/webapp/js/polyfill/nashorn-polyfill.js")),
      ScriptURL(getClass.getResource("/webapp/js/bundle.js")),
      ScriptText("var frontend = new com.nudemeth.example.web.Frontend();")
    )
  )

  get("/") {
    val model = write(HomeViewModel(s"This is Home page"))
    val content = nashorn.invokeMethod[String]("frontend", "renderServer", "/", model)
    views.html.index.render(content, model)
  }

  get("/about") {
    val model = write(HomeViewModel(s"This is About page"))
    val content = nashorn.invokeMethod[String]("frontend", "renderServer", "/about", model)
    views.html.index.render(content, model)
  }
}
