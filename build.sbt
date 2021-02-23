
organization := "fink"
name := "fink"
version := "2.0.0-SNAPSHOT"
scalaVersion := "2.13.3"

val catsVersion = "2.1.1"
val doobieVersion = "0.9.2"
val metricsVersion = "3.0.2"
val circeVersion = "0.13.0"
val http4sVersion = "0.21.7"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.pauldijou" %% "jwt-circe" % "4.1.0",

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
  "joda-time" % "joda-time" % "2.4",

  "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
  "org.specs2" %% "specs2-core" % "4.6.0" % "test",
)

lazy val root = project.in(file("."))

