import Dependencies.*

lazy val root = (project in file("."))
  .settings(
    name                 := "transactions-scoring",
    scalaVersion         := "2.13.9",
    libraryDependencies ++= commons,
  )
