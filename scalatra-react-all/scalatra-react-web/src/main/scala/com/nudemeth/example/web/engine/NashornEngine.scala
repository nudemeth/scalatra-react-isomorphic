package com.nudemeth.example.web.engine

import javax.script.{CompiledScript, ScriptEngineManager}

import jdk.nashorn.api.scripting.{JSObject, NashornScriptEngine}

object NashornEngine {
  /*
  Shared engine and compiled script. See link below
  https://stackoverflow.com/questions/30140103/should-i-use-a-separate-scriptengine-and-compiledscript-instances-per-each-threa
  https://blogs.oracle.com/nashorn/nashorn-multithreading-and-mt-safety
  */
  private val manager: ScriptEngineManager = new ScriptEngineManager()
  private val engine: NashornScriptEngine = manager.getEngineByName("nashorn").asInstanceOf[NashornScriptEngine]
  private var compiledScript: Option[CompiledScript] = None
}

class NashornEngine(scripts: Seq[ScriptSource]) extends JavaScriptEngine(scripts) {
  import NashornEngine._

  private def init(): Unit = {
    NashornEngine.compiledScript match {
      case Some(_) =>
      case None => NashornEngine.compiledScript = Some(compileScript())
    }
  }

  private def compileScript(): CompiledScript = {
    val allScript = scripts.map{
      case ScriptText(ss) => ss
      case ScriptURL(ss) => scala.io.Source.fromURL(ss)("UTF-8").mkString
    }.mkString(sys.props("line.separator"))
    NashornEngine.engine.compile(allScript)
  }

  init()

  /**
    * To invoke javascript object method. Always create new bindings when calling this because of multithreading environment
    * @param objectName Object to call
    * @param methodName Method name to call
    * @param args Parameters to pass into the method
    * @tparam T Expected return type
    * @return Instance of expected T type
    */
  override def invokeMethod[T](objectName: String, methodName: String, args: Any*): T = {
    val bindings = engine.createBindings()
    compiledScript.get.eval(bindings)
    val obj = bindings.get(objectName).asInstanceOf[JSObject]
    val method = obj.getMember(methodName).asInstanceOf[JSObject]
    method.call(obj, args.map(_.asInstanceOf[AnyRef]): _*).asInstanceOf[T]
  }

}

