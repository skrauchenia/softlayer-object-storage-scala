name := "softlayer-object-storage-scala"

organization := "softlayer"

version := "0.5beta-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.6" % "test"

libraryDependencies += "com.typesafe" % "config" % "1.0.2"

publishTo := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", "", "")
