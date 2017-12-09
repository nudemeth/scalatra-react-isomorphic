package com.nudemeth.example.web.controller

import com.nudemeth.example.web.engine._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet

trait BaseController extends ScalatraServlet {

  implicit val jsonFormats: Formats = DefaultFormats

  protected val renderer: JavaScriptEngine = J2V8Engine.registerScripts(
    Seq(
      ScriptURL(getClass.getResource("/webapp/js/polyfill/j2v8-polyfill.js")),
      ScriptURL(getClass.getResource("/webapp/js/bundle.js")),
      ScriptText("var frontend = new com.nudemeth.example.web.Frontend();")
    )
  )

  override def destroy(): Unit = {
    renderer.destroy
    super.destroy()
  }

}
