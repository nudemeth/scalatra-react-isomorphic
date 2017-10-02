package com.nudemeth.example.launcher

import org.eclipse.jetty.server.{Connector, Server, ServerConnector}
import org.eclipse.jetty.webapp.WebAppContext

object JettyLauncher {
  private lazy val server: Server = new Server()
  private lazy val connector: ServerConnector = new ServerConnector(server)
  private lazy val context: WebAppContext = new WebAppContext()
  private lazy val resourceLocation: String = getClass.getResource("/webapp").toExternalForm

  def main(args: Array[String]): Unit = {
    setConnector()
    setServer()
    setContext()
    startServer()
  }

  private def getPort(): Int = {
    8080
  }

  private def setConnector(): Unit = {
    connector.setPort(getPort())
  }

  private def setServer(): Unit = {
    server.setConnectors(Array[Connector](connector))
    server.setHandler(context)
  }

  private def setContext(): Unit = {
    context.setServer(server)
    context.setDescriptor(resourceLocation + "/WEB-INF/web.xml")
    context.setContextPath("/")
    context.setResourceBase(resourceLocation)
    context.setParentLoaderPriority(true)
  }

  private def startServer(): Unit = {
    server.start()
    server.join()
  }
}
