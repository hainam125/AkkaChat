name := "chat-data"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.18"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "junit" % "junit" % "4.12")
