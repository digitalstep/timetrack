name := "timetrack"
version := "1.0"
isSnapshot := true

scalaVersion := "2.11.7"
crossScalaVersions := Seq("2.11.7")

mainClass := Some("de.digitalstep.timetrack.Application")

libraryDependencies ++= Seq(
  "org.parboiled" %% "parboiled" % "2.1.0",
  "org.scalafx" %% "scalafx" % "8.0.60-R9",
  "org.controlsfx" % "controlsfx" % "8.40.9",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "org.scalacheck" %% "scalacheck" % "1.12.5" % Test
)
