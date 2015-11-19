name := "timetrack"
version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.parboiled" %% "parboiled" % "2.1.0",
  "org.scalafx" %% "scalafx" % "8.0.60-R9",
  "org.controlsfx" % "controlsfx" % "8.40.9",
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "org.scalacheck" %% "scalacheck" % "1.12.5" % Test
)
