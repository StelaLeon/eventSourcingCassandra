import akka.persistence
import sbt.Keys._

name := """twitter-service"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

libraryDependencies += "com.ning" % "async-http-client" % "1.9.29"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.0"


libraryDependencies += "joda-time" % "joda-time" % "2.7"

libraryDependencies += "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.85"

libraryDependencies += "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "0.85" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-persistence-experimental" % "2.4-M2"

val cassandraDriverVersion = "3.0.0"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.5.13"
