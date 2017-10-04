val commonSettings = Seq (
  organization := "com.nudemeth",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3",
  resolvers += Classpaths.typesafeReleases
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime"
)

def getFromSelectedProjects[T](targetTask: TaskKey[T], state: State, project: ProjectRef) = {
  val extracted = Project.extract(state)
  val structure = extracted.structure
  val childrenProjects = Project.getProject(project, structure).toSeq.flatMap(_.uses).distinct
  childrenProjects.map(p => Def.task {
    (targetTask in p).value
  } evaluate structure.data).join.asInstanceOf[Task[Seq[File]]]
}


lazy val pack = taskKey[Unit]("Copy runtime dependencies and built artifacts to target/output") := {
  val artifact = packageBin.in(Runtime).value
  val libs = dependencyClasspath.in(Runtime).value.map(_.data).filter(_.isFile).toList
  streams.value.log.info(s"Copying libs to ${baseDirectory.value}/target/output/lib")
  streams.value.log.info(s"Copying ${artifact.getName} to ${baseDirectory.value}/target/output/core")
  IO.copy(libs.map(f => (f, baseDirectory.value / s"/target/output/lib/${f.getName}")))
  IO.copyFile(artifact, baseDirectory.value / s"/target/output/core/${artifact.getName}")

  val childrenJars = Def.taskDyn {
    val children = getFromSelectedProjects(packageBin in Runtime, state.value, thisProjectRef.value)
    Def.task {children.value}
  }.value

  streams.value.log.info(s"Copying: ${childrenJars.foldLeft("")((prev,next) => s"$prev, $next")}")
  IO.copy(childrenJars.map(j => (j, baseDirectory.value / s"/target/output/core/${j.getName}")))
}

/*test in Test := {
pack.init.value
}*/

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

