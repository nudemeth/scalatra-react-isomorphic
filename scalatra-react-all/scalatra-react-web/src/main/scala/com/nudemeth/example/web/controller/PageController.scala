package com.nudemeth.example.web.controller

import com.nudemeth.example.web.engine.NashornEngine
import org.scalatra._

class PageController extends ScalatraServlet {

  private lazy val nashorn: NashornEngine = new NashornEngine()

  nashorn.registerScript(getClass.getResource("/webapp/js/polyfill/nashorn-polyfill.js"))
  nashorn.registerScript(getClass.getResource("/webapp/js/bundle.js"))
  nashorn.registerExpression("var frontend = new com.nudemeth.example.web.Frontend();")

  get("/") {
    val content = nashorn.invokeMethod("frontend", "renderServer", "Hello World")
    val data = "\"Hello World\""
    views.html.index.render(content, data)
  }

}
