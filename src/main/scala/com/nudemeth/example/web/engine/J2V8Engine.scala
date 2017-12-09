package com.nudemeth.example.web.engine

import com.eclipsesource.v8.{V8, V8Array, V8Locker}

object J2V8Engine {
  private[this] var instance: Option[J2V8Engine] = None
  def registerScripts(scripts: Seq[ScriptSource]): J2V8Engine = {
    instance match {
      case Some(_) =>
      case None => instance = Some(new J2V8Engine(scripts))
    }
    instance.get
  }
}

sealed class J2V8Engine private(scripts: Seq[ScriptSource]) extends JavaScriptEngine(scripts) {
  private val engine = V8.createV8Runtime()
  private val allScripts = scripts.map {
    case ScriptText(s) => s
    case ScriptURL(s) => scala.io.Source.fromURL(s)("UTF-8").mkString
  }.mkString(sys.props("line.separator"))

  engine.executeVoidScript(allScripts)
  engine.getLocker.release()

  override def invokeMethod[T](objectName: String, methodName: String, args: Any*) = synchronized {
    engine.getLocker.acquire()
    val obj = engine.getObject(objectName)
    val paramz = new V8Array(engine)
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
    engine.getLocker.release()
    result
  }

  override def destroy: Unit = {
    engine.release()
    super.destroy
  }
}
