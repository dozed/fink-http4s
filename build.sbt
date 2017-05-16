
organization := "fink"
name := "fink"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.2"

val ScalatraVersion = "2.5.0"
val catsVersion = "0.9.0"
val doobieVersion = "0.4.0"
val metricsVersion = "3.0.2"
val circeVersion = "0.7.1"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",

  "org.typelevel" %% "cats" % catsVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-core-cats" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,

  "org.http4s" %% "http4s-core" % "0.17.0-M2",
  "com.lihaoyi" %% "scalatags" % "0.6.5",

  "commons-io" % "commons-io" % "2.0.1",
  "com.typesafe" % "config" % "1.3.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "joda-time" % "joda-time" % "2.4",

  "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided;test"
)

containerPort := 8090

enablePlugins(JettyPlugin)

lazy val root = project.in(file("."))

