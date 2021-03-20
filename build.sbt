
organization := "fink"
name := "fink"
version := "2.0.0-SNAPSHOT"
scalaVersion := "2.13.3"

val doobieVersion = "0.10.0"
val circeVersion = "0.13.0"
val http4sVersion = "0.21.20"
val specs2Version = "4.10.6"

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.pauldijou" %% "jwt-circe" % "5.0.0",

  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,

  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,

  "commons-io" % "commons-io" % "2.0.1",
  "com.typesafe" % "config" % "1.3.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.apache.tika" % "tika-core" % "1.12",
  "com.sksamuel.scrimage" % "scrimage-core" % "4.0.17",
  "com.sksamuel.scrimage" %% "scrimage-scala" % "4.0.17",
  "joda-time" % "joda-time" % "2.4",

  "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
  "org.specs2" %% "specs2-core" % specs2Version % "test",
  "org.specs2" %% "specs2-scalacheck" % specs2Version % "test",
  "io.circe" %% "circe-testing" % circeVersion % "test",
  "org.typelevel" %% "discipline-specs2" % "1.1.4" % "test",
  "com.h2database" % "h2" % "1.4.200" % "test",
)

scalacOptions += "-deprecation"

assemblyJarName in assembly := "backend.jar"

mainClass in assembly := Some("fink.Http4sLauncher")

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case "application.conf" => MergeStrategy.discard
  case "logback.xml" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

lazy val root = project.in(file("."))

