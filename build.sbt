/*
 * Copyright 2020 acme.com
 */

import sbt.Keys.{libraryDependencies, _}
import sbt._
import sbtassembly.AssemblyPlugin.autoImport.{ShadeRule, _}

name := "Scala.CodeCamp.Spark.Demo"

lazy val appVersion = "0.0.1"
lazy val sparkVersion = "2.4.4"
lazy val hadoopVersion = "2.7.3"
lazy val scalaTestVersion = "3.0.1"
lazy val log4jVersion = "2.8.1"

fork in Test := true
parallelExecution in Test := false // needed otherwise spark based unit tests will fail
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

// make it easy to share as a library locally with other projects
publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository")))

publishMavenStyle := true

// may need to sh:> export SBT_OPTS="-Xmx4G"
javaOptions ++= Seq("-Xms512M", "-Xmx4048M", "-XX:MaxPermSize=4048M", "-XX:+CMSClassUnloadingEnabled")

exportJars := true

logLevel in assembly := Level.Debug

lazy val commonSettings = Seq(
  organization := "com.acme",
  version := appVersion,
  scalaVersion := "2.11.7",

  resolvers ++= Seq(
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    Resolver.jcenterRepo,
    Resolver.sbtPluginRepo("releases")
  )
)

lazy val root = (project in file(".")).
  disablePlugins(AssemblyPlugin).
  aggregate(
    spark_commons,
    spark_jobs_word_count
  ).
  settings(commonSettings: _*).
  settings(
    aggregate in update := false
  )

lazy val spark_commons = (project in file("spark_commons")).
  disablePlugins(AssemblyPlugin).
  settings(commonSettings: _*).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*).
  settings(
    libraryDependencies ++= Seq(

      "org.slf4j" % "slf4j-api" % "1.7.21"

      , "commons-logging" % "commons-logging" % "1.2"

      , "org.apache.commons" % "commons-lang3" % "3.6"

      , "org.apache.logging.log4j" % "log4j-1.2-api" % log4jVersion

      , "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion

      , "org.apache.logging.log4j" % "log4j-api" % log4jVersion

      , "org.apache.logging.log4j" % "log4j-core" % log4jVersion

      , "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"

      , "com.typesafe" % "config" % "1.3.1"

      , "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
        excludeAll(
          ExclusionRule(organization = "com.sun.jersey")
          , ExclusionRule(organization = "com.sun.jersey.contribs")
          , ExclusionRule(organization = "com.amazonaws")
          , ExclusionRule(organization = "org.apache.hadoop")
      )

      , "org.apache.hadoop" % "hadoop-client" % hadoopVersion % "provided"

      , "org.scalatest" %% "scalatest" % scalaTestVersion % "test"

    ).map(_.exclude("org.slf4j", "slf4j-log4j12"))
  )

lazy val spark_jobs_word_count = (project in file("spark_jobs_word_count")).
  dependsOn(spark_commons).
  settings(commonSettings: _*).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*).
  settings(
    libraryDependencies ++= Seq(

      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
        excludeAll(
        ExclusionRule(organization = "com.sun.jersey")
        , ExclusionRule(organization = "com.sun.jersey.contribs")
        , ExclusionRule(organization = "com.amazonaws")
        , ExclusionRule(organization = "org.apache.hadoop")
      )

      , "org.apache.hadoop" % "hadoop-client" % hadoopVersion % "provided"

      , "org.apache.hadoop" % "hadoop-aws" % hadoopVersion % "provided"
        excludeAll(
        ExclusionRule(organization = "com.sun.jersey")
        , ExclusionRule(organization = "com.sun.jersey.contribs")
      )
      , "org.rogach" %% "scallop" % "3.2.0"

      , "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    ).map(_.exclude("org.slf4j", "slf4j-log4j12"))
      .map(_.exclude("commons-logging", "commons-logging"))
  ).
  settings(
    test in assembly := {},
    assemblyShadeRules in assembly := Seq(
      ShadeRule.rename("com.fasterxml.jackson.**" -> "shady_jackson_shade.@1").inAll
    ),
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
      case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
      case PathList(ps@_*) if ps.last endsWith ".class" => MergeStrategy.first
      case PathList(ps@_*) if ps.last endsWith ".txt" => MergeStrategy.first
      case PathList(ps@_*) if ps.last endsWith ".tooling" => MergeStrategy.first
      case PathList(ps@_*) if ps.last endsWith ".properties" => MergeStrategy.first
      case "mime.types" => MergeStrategy.first
      case "log4j2.xml" => MergeStrategy.first
      case "reference.conf" => MergeStrategy.concat
      case "application.conf" => MergeStrategy.concat
      case PathList("META-INF", xs@_*) =>
        xs map {
          _.toLowerCase
        } match {
          case ("log4j-provider.properties" :: Nil) =>
            MergeStrategy.first
          case ("mailcap" :: Nil) =>
            MergeStrategy.first
          case _ => MergeStrategy.discard
        }
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )


