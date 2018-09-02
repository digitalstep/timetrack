enablePlugins(JavaAppPackaging, JDKPackagerPlugin)

ThisBuild / name                := "timetrack"
ThisBuild / version             := "0.1"
ThisBuild / isSnapshot          := true

ThisBuild / scalaVersion        := "2.12.6"

ThisBuild / maintainer          := "Gunnar Bastkowski <gunnar@digitalstep.de>"
ThisBuild / packageDescription  := "A simple tool with a journal to track time spent on projects"
ThisBuild / packageSummary in Linux := "Time Tracking"

lazy val root = (project in file(".")).
  configs(IntegrationTest).
  settings(Defaults.itSettings)

root / mainClass := Some("de.digitalstep.timetrack.Application")
root / libraryDependencies ++= Seq(
  "org.parboiled"               %% "parboiled"      %  "2.1.4",
  "org.scalafx"                 %%  "scalafx"       % "8.0.144-R12",
  "org.controlsfx"              %   "controlsfx"    % "9.0.0",
  "com.typesafe.scala-logging"  %% "scala-logging"  % "3.9.0",
  "ch.qos.logback"              % "logback-classic" % "1.2.3",
  "org.scalatest"               %% "scalatest"      % "3.0.5"   % "it,test",
  "org.scalacheck"              %% "scalacheck"     % "1.14.0"  % "it,test",
  "org.testfx"                  % "testfx-core"     % "4.0.+"   % "it,test",
  "org.testfx"                  % "testfx-junit"    % "4.0.+"   % "it,test")
