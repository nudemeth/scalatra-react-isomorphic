val commonSettings = Seq (
  organization := "com.nudemeth",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3",
  resolvers += Classpaths.typesafeReleases
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime"
)

lazy val pack = taskKey[Unit]("Copy runtime dependencies and built artifacts to target/output") := {
  val artifact = packageBin.in(Runtime).value
  val libs = dependencyClasspath.in(Runtime).value.map(_.data).filter(_.isFile).toList
  streams.value.log.info(s"Copying libs to $baseDirectory/target/output/lib")
  streams.value.log.info(s"Copying ${artifact.getName} to $baseDirectory/target/output/core")
  IO.copy(libs.map(f => (f, baseDirectory.value / s"/target/output/lib/${f.getName}")))
  IO.copyFile(artifact, baseDirectory.value / s"/target/output/core/${artifact.getName}")
}

test in Test := {
  pack.init.value
}

lazy val all = (project in file("."))
  .settings(
    commonSettings,
    name := "scalatra-react-all",
    pack,
  )
  .dependsOn(web, launcher)
  .aggregate(web, launcher)

lazy val launcher = (project in file("./scalatra-react-launcher"))
  .settings(
    commonSettings,
    name := "scalatra-react-launcher",
    pack
  )
  .dependsOn(web)
  .aggregate(web)

lazy val web = (project in file("./scalatra-react-web"))
  .settings(
    commonSettings,
    name := "scalatra-react-web",
    pack
  )

