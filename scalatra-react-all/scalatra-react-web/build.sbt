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

lazy val packFrontend = taskKey[Unit]("Copy front-end resources to src/main/resources folder") := {
  compile.in(Compile).value
  val source = sourceDirectory.value / "main/webapp"
  IO.copyDirectory(source, resourceDirectory.in(Compile).value / "webapp")
}

compile.in(Compile) := {
  compileFrontend.init.value
  compile.in(Compile).value
}

packageBin.in(Compile) := {
  packFrontend.init.value
  packageBin.in(Compile).value
}