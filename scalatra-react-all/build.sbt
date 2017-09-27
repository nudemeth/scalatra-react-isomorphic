val commonSettings = Seq (
  organization := "com.nudemeth",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3"
)

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
)

lazy val all = (project in file("."))
  .settings(
    commonSettings,
    name := "scalatra-react-all"
  )
  .dependsOn(web)
  .aggregate(web)

lazy val web = (project in file("./scalatra-react-web"))
  .settings(
    commonSettings,
    name := "scalatra-react-web"
  )
