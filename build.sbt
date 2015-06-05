name := "softlayer-object-storage-scala"

organization := "com.cherrit"

version := "0.7.4"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.4", "2.11.6")

libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"

libraryDependencies += "org.specs2" %% "specs2-core" % "2.3.11" % "test"

libraryDependencies += "com.typesafe" % "config" % "1.0.2"

publishTo := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", "", "")
