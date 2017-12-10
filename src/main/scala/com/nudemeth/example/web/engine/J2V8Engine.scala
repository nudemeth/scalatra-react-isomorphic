package com.nudemeth.example.web.engine

import com.eclipsesource.v8.{V8, V8Array}

object J2V8Engine {
  val instance: JavaScriptEngine = new J2V8Engine()
}

sealed class J2V8Engine private(allScripts: Option[String] = None, engine: Option[V8] = None) extends JavaScriptEngine {

  override def registerScripts(scripts: Seq[ScriptSource]): JavaScriptEngine = {
    new J2V8Engine(Some(scripts.map{
      case ScriptText(s) => s
      case ScriptURL(s) => scala.io.Source.fromURL(s)("UTF-8").mkString
    }.mkString(sys.props("line.separator"))))
  }

  override def build: J2V8Engine = {
    if (allScripts.isEmpty) {
      throw new UnsupportedOperationException("No scripts have been registered. Please call registerScripts method first.")
    }

    val engine = V8.createV8Runtime()
    engine.executeVoidScript(allScripts.get)
    engine.getLocker.release()

    new J2V8Engine(allScripts, Some(engine))
  }

  override def invokeMethod[T](objectName: String, methodName: String, args: Any*): T = synchronized {
    engine.get.getLocker.acquire()
    val obj = engine.get.getObject(objectName)
    val paramz = new V8Array(engine.get)
    args.foldLeft(paramz)((params, value) => {
      value match {
        case s:String => params.push(s)
        case i:Int => params.push(i)
        case d:Double => params.push(d)
        case b:Boolean => params.push(b)
        case o => throw new IllegalArgumentException(s"Object type ${o.getClass.getName} is not supported")
      }
    })
    val result = obj.executeStringFunction(methodName, paramz).asInstanceOf[T]
    paramz.release()
    obj.release()
    engine.get.getLocker.release()
    result
  }

  override def destroy: Unit = {
    engine.get.release()
    super.destroy
  }
}
