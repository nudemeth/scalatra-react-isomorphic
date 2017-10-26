package com.nudemeth.example.web.engine

abstract class JavaScriptEngine(scripts: Seq[ScriptSource]) {
  def invokeMethod[T](objectName: String, methodName: String, args: Any*): T
}
