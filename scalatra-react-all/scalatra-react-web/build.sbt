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

lazy val copyFrontEndResources = taskKey[Unit]("Copy front-end resources to src/main/resources folder") := {
  val source = baseDirectory.value / "src/main/webapp"
  IO.copyDirectory(source, resourceDirectory.in(Compile).value / "webapp")
}

compile.in(Compile) := {
  copyFrontEndResources.init.value
  compile.in(Compile).value
}