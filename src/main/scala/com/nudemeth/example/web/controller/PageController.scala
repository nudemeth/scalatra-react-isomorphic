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
    val model = write(HomeViewModel("This is content"))
    val content = nashorn.invokeMethod[String]("frontend", "renderServer", model)
    views.html.index.render(content, model)
  }
}
