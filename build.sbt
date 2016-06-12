name := "formula1"

mainClass in Compile := Some("main.scala.com.github.jasimvs.formula1.Main")

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"
