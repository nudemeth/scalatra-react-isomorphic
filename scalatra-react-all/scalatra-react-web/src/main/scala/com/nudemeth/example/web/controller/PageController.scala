package com.nudemeth.example.web.controller

import com.nudemeth.example.web.engine._
import org.scalatra._

class PageController extends ScalatraServlet {

  private lazy val nashorn: JavaScriptEngine = new NashornEngine(
    Seq(
      ScriptURL(getClass.getResource("/webapp/js/polyfill/nashorn-polyfill.js")),
      ScriptURL(getClass.getResource("/webapp/js/bundle.js")),
      ScriptText("var frontend = new com.nudemeth.example.web.Frontend();")
    )
  )

  get("/") {
    val content = nashorn.invokeMethod[String]("frontend", "renderServer", "Hello World")
    val data = "\"Hello World\""
    views.html.index.render(content, data)
  }

}
