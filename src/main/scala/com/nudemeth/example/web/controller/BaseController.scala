package com.nudemeth.example.web.controller

import com.nudemeth.example.web.engine.{JavaScriptEngine, NashornEngine, ScriptText, ScriptURL}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet

trait BaseController extends ScalatraServlet {

  implicit val jsonFormats: Formats = DefaultFormats

  protected val renderer: JavaScriptEngine = NashornEngine(
    Seq(
      ScriptURL(getClass.getResource("/webapp/js/polyfill/nashorn-polyfill.js")),
      ScriptURL(getClass.getResource("/webapp/js/bundle.js")),
      ScriptText("var frontend = new com.nudemeth.example.web.Frontend();")
    )
  )

}
