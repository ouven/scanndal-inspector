
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

organization := "de.aktey.scanndal"

name := "scanndal-inspector"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.4"

mainClass := Some("de.aktey.scanndal.inspector.Inspector")

assemblyJarName in assembly := "Inspector.jar"

homepage := Some(url("https://github.com/ouven/scanndal-inspector/wiki"))

licenses := Seq("Apache License Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
  "The New BSD License" -> url("http://www.opensource.org/licenses/bsd-license.html"))

libraryDependencies ++= Seq(
  "de.aktey.scanndal" %% "scanndal" % "latest.release",
  "org.scalafx" %% "scalafx" % "8.0.20-R6",
  "org.specs2" %% "specs2" % "latest.release" % "test"
)
