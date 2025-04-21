ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "scala-final-project-2025",
    libraryDependencies ++= Seq("com.softwaremill.sttp.client4" %% "core" % "4.0.0-M7",
      "au.com.onegeek" % "sbt-dotenv" % "2.1.192"
    )

  )
