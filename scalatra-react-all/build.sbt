import sbt.Package.ManifestAttributes

val commonSettings = Seq (
  organization := "com.nudemeth",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3",
  resolvers += Classpaths.typesafeReleases,
  packageOptions.in(packageBin) += addManifestClassPath.init.value
)

packageOptions.in(packageBin) += Package.ManifestAttributes("Main-Class" -> "com.nudemeth.example.launcher.JettyLauncher")

def getFromSelectedProjects[T](targetTask: TaskKey[T], state: State, project: ProjectRef) = {
  val extracted = Project.extract(state)
  val structure = extracted.structure
  val childrenProjects = Project.getProject(project, structure).toSeq.flatMap(_.uses).distinct
  childrenProjects.map(p => Def.task {
    (targetTask in p).value
  } evaluate structure.data).join.asInstanceOf[Task[Seq[File]]]
}

lazy val getLibJars = taskKey[Seq[File]]("Get dependency lib jars") := {
  dependencyClasspath.in(Runtime).value.map(_.data).filter(_.isFile).toList
}

lazy val getSubProjectJars = taskKey[Seq[File]]("Get dependency sub project jars") := {
  Def.taskDyn {
    val children = getFromSelectedProjects(packageBin in Runtime, state.value, thisProjectRef.value)
    Def.task {children.value}
  }.value
}

lazy val pack = taskKey[Unit]("Copy runtime dependencies and built artifacts to target/output") := {
  val subProjects = getSubProjectJars.init.value
  val libs = getLibJars.init.value
  val artifacts = Seq(packageBin.in(Runtime).value)
  val copiedLibs = IO.copy(libs.map(f => (f, baseDirectory.value / s"/target/output/lib/${f.getName}")))
  val copiedArtifacts = IO.copy(artifacts.map(f => (f, baseDirectory.value / s"/target/output/${f.getName}")))
  val copiedSubProjects = IO.copy(subProjects.map(j => (j, baseDirectory.value / s"/target/output/core/${j.getName}")))

  streams.value.log.info(copiedLibs.foldLeft("")((s,f) => { s"$s${name.value} --> Copied lib ${f.getName} to ${f.getParent}\n" }).trim)
  streams.value.log.info(copiedArtifacts.foldLeft("")((s,f) => { s"$s${name.value} --> Copied artifact ${f.getName} to ${f.getParent}\n" }).trim)
  streams.value.log.info(copiedSubProjects.foldLeft("")((s,f) => { s"$s${name.value} --> Copied dependent project jar ${f.getName} to ${f.getParent}\n" }).trim)
}

lazy val libClassPath = settingKey[String]("Relative path to lib jars for adding to Class-Path attribute in manifest")
lazy val coreClassPath = settingKey[String]("Relative path to sub project jars for adding to Class-Path attribute in manifest")

lazy val addManifestClassPath = taskKey[ManifestAttributes]("Add manifest Class-Path attribute") := {
  val subProjects = getSubProjectJars.init.value.map(f => s"${coreClassPath.value}${f.getName}")
  val libs = getLibJars.init.value.map(f => s"${libClassPath.value}${f.getName}")
  val dependencies = subProjects ++ libs
  Package.ManifestAttributes("Class-Path" -> dependencies.mkString(" "))
}

lazy val all = (project in file("."))
.settings(
  commonSettings,
  name := "scalatra-react-all",
  coreClassPath := "./core/",
  libClassPath := "./lib/",
  pack
)
.dependsOn(web, launcher)
.aggregate(web, launcher)

lazy val launcher = (project in file("./scalatra-react-launcher"))
.settings(
  commonSettings,
  name := "scalatra-react-launcher",
  coreClassPath := "./",
  libClassPath := "../lib/",
)
.dependsOn(web)
.aggregate(web)

lazy val web = (project in file("./scalatra-react-web"))
.settings(
  commonSettings,
  name := "scalatra-react-web",
  coreClassPath := "./",
  libClassPath := "../lib/",
)

