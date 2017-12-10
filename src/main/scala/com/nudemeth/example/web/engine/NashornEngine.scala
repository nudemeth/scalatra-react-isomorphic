package com.nudemeth.example.web.engine

import javax.script.CompiledScript

import jdk.nashorn.api.scripting.{NashornScriptEngine, NashornScriptEngineFactory, ScriptObjectMirror}

object NashornEngine {
  val instance: JavaScriptEngine = new NashornEngine()
}

sealed class NashornEngine private(allScripts: Option[String] = None, engine: Option[NashornScriptEngine] = None, compiledScript: Option[CompiledScript] = None) extends JavaScriptEngine {
  /*
    Shared engine and compiled script. See links below:
    https://stackoverflow.com/questions/30140103/should-i-use-a-separate-scriptengine-and-compiledscript-instances-per-each-threa
    https://blogs.oracle.com/nashorn/nashorn-multithreading-and-mt-safety
  */
  override def registerScripts(scripts: Seq[ScriptSource]): JavaScriptEngine = {
    new NashornEngine(Some(scripts.map{
      case ScriptText(s) => s
      case ScriptURL(s) => scala.io.Source.fromURL(s)("UTF-8").mkString
    }.mkString(sys.props("line.separator"))))
  }

  override def build: NashornEngine = {
    if (allScripts.isEmpty) {
      throw new UnsupportedOperationException("No scripts have been registered. Please call registerScripts method first.")
    }
    val engine = new NashornScriptEngineFactory()
      .getScriptEngine("-strict", "--no-java", "--no-syntax-extensions")
      .asInstanceOf[NashornScriptEngine]
    val compiledScript = engine.compile(allScripts.get)
    new NashornEngine(allScripts, Some(engine), Some(compiledScript))
  }

  /**
    * Invoking javascript object method. Always create new bindings when calling this because of multithreading environment
    * @param objectName Object to call
    * @param methodName Method name to call
    * @param args Parameters to pass into the method
    * @tparam T Expected return type
    * @return Instance of expected T type
    */
  override def invokeMethod[T](objectName: String, methodName: String, args: Any*): T = {
    val bindings = engine.get.createBindings()
    compiledScript.get.eval(bindings)
    val obj = bindings.get(objectName).asInstanceOf[ScriptObjectMirror]
    obj.callMember(methodName, args.map(_.asInstanceOf[AnyRef]): _*).asInstanceOf[T]
  }

}