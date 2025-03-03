ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "com.example"

lazy val learningScaFi = (project in file("."))
  .settings(
    name := "learningScaFi",
    libraryDependencies ++= Seq(
      "it.unibo.scafi" %% "scafi-core" % "1.2.0",
      "it.unibo.scafi" %% "scafi-simulator" % "1.2.0"
    ),
  )

// Add ScaFi's repository to resolve dependencies
resolvers += "jitpack" at "https://jitpack.io"

