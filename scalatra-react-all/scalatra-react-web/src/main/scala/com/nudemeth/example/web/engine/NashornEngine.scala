package com.nudemeth.example.web.engine

import java.io.Reader
import java.net.URL
import javax.script.ScriptEngineManager

import jdk.nashorn.api.scripting.{JSObject, NashornScriptEngine}

object NashornEngine {
  private val manager: ScriptEngineManager = new ScriptEngineManager()
  private val engine: NashornScriptEngine = manager.getEngineByName("nashorn").asInstanceOf[NashornScriptEngine]
}

class NashornEngine {
  def registerScript(scriptPath: URL): Unit = {
    NashornEngine.engine.eval(readScriptAsStream(scriptPath))
  }

  def registerExpression(command: String): Unit = {
    NashornEngine.engine.eval(command)
  }

  def invokeFunction(functionName: String, args: Any*): String = {
    val returnedValue: AnyRef = NashornEngine.engine.invokeFunction(functionName, args.map(_.asInstanceOf[AnyRef]): _*)
    String.valueOf(returnedValue)
  }

  def invokeMethod(objectName: String, methodName: String, args: Any*): String = {
    val jsObject =  NashornEngine.engine.eval(objectName).asInstanceOf[JSObject]
    val returnedValue: AnyRef = NashornEngine.engine.invokeMethod(jsObject, methodName, args.map(_.asInstanceOf[AnyRef]): _*)
    String.valueOf(returnedValue)
  }

  def getDeepestMember(objectName: String, parent: Option[JSObject] = None): JSObject = {
    val parts = objectName.split(".")
    val member = (parent, parts.size) match {
      case (None, size) if size == 1 => NashornEngine.engine.get(parts(0)).asInstanceOf[JSObject]
      case (None, size) if size > 1 => getDeepestMember(parts.drop(1).mkString("."), Some(NashornEngine.engine.get(parts(0)).asInstanceOf[JSObject]))
      case (Some(parent), size) if size == 1 => parent.getMember(parts(0)).asInstanceOf[JSObject]
      case (Some(parent), size) if size > 1 => getDeepestMember(parts.drop(1).mkString("."), Some(parent.getMember(parts(0)).asInstanceOf[JSObject]))
    }
    member
  }

  private def readScriptAsStream(path: URL): Reader = {
    scala.io.Source.fromURL(path)("UTF-8").reader()
  }
}
