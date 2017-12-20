package com.nudemeth.example.web.controller

import com.nudemeth.example.web.engine._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.slf4j.{Logger, LoggerFactory}

trait BaseController extends ScalatraServlet {

  implicit val jsonFormats: Formats = DefaultFormats
  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  protected val renderer: JavaScriptEngine = J2V8Engine.instance.registerScripts(
    Seq(
      ScriptURL(getClass.getResource("/webapp/js/polyfill/j2v8-polyfill.js")),
      ScriptURL(getClass.getResource("/webapp/js/bundle.js")),
      ScriptText("var frontend = new com.nudemeth.example.web.Frontend();")
    )
  ).build

  override def destroy(): Unit = {
    renderer.destroy
    super.destroy()
  }

}
