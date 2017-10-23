import scala.sys.process._

val ScalatraVersion = "2.5.1"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)

val commands = if (sys.props("os.name").contains("Windows")) Seq("cmd", "/c") else Seq ("bash", "-c")

lazy val installFrontendLib = taskKey[Unit]("Install front-end lib by using npm") := {
  val task = streams.value
  val npmInstallCmd = commands :+ "npm install"
  val npmInstall = Process(npmInstallCmd, sourceDirectory.value / "main/node")
  task.log.info("Installing front-end lib")
  if ((npmInstall ! task.log) != 0) {
    throw new IllegalStateException("Front-end build failed: Cannot install lib from npm")
  }
}

lazy val compileFrontend = taskKey[Unit]("Compile all fron-end resources") := {
  installFrontendLib.init.value
  val task = streams.value
  val webpackCmd = commands :+ s"${sourceDirectory.value}/main/node/node_modules/.bin/webpack --bail --config ${sourceDirectory.value}/main/node/webpack/webpack.config.js"
  val webpack = webpackCmd
  task.log.info("Compiling front-end resources")
  if ((webpack ! task.log) != 0) {
    throw new IllegalStateException("Front-end build failed: Compilation failed!")
  }
}

lazy val copyNashornPolyfill = taskKey[Unit]("Copy polyfill for Nashorn javascript engine") := {
  val source = sourceDirectory.value / "main/frontend/js/polyfill"
  IO.copyDirectory(source, sourceDirectory.value / "main/webapp/js/polyfill")
}

lazy val buildFrontEndResource = taskKey[Seq[File]]("Generate front-end resources") := {
  compileFrontend.init.value
  copyNashornPolyfill.init.value
  val webapp = sourceDirectory.value / "main" / "webapp"
  val managed = resourceManaged.value
  for {
    (from, to) <- webapp ** "*" pair Path.rebase(webapp, managed / "main" / "webapp")
  } yield {
    Sync.copy(from, to)
    to
  }
}

resourceGenerators.in(Compile) += buildFrontEndResource.init