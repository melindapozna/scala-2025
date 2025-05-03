ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.4"
val circeVersion = "0.14.9"


lazy val root = (project in file("."))
  .settings(
    name := "scala-final-project-2025",
    libraryDependencies ++= Seq("com.softwaremill.sttp.client4" %% "core" % "4.0.0-M7"
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)

  )
