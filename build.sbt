ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"

val ZioVersion        = "1.0.11"
val ZioPreludeVersion = "1.0.0-RC6"
val ZioMagicVersion   = "0.3.2"
val ZioDependencies = Def.setting {
  Seq(
    "dev.zio"              %% "zio"         % ZioVersion,
    "dev.zio"              %% "zio-streams" % ZioVersion,
    "dev.zio"              %% "zio-prelude" % ZioPreludeVersion,
    "io.github.kitlangton" %% "zio-magic"   % ZioMagicVersion
  )
}

val AkkaVersion     = "2.6.15"
val AkkaHttpVersion = "10.2.6"
val AkkaDependencies = Def.setting {
  Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream"      % AkkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"       % AkkaVersion,
    "com.typesafe.akka" %% "akka-http"        % AkkaHttpVersion
  )
}

val TapirVersion = "0.18.3"
val TapirDependencies = Def.setting {
  Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core"                 % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe"           % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-zio"                  % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server"     % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"         % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"   % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % TapirVersion
  )
}

val CirceVersion = "0.13.0"
val CirceDependencies = Def.setting {
  Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-generic-extras",
    "io.circe" %% "circe-parser"
  ).map(_ % CirceVersion)
}

lazy val root = (project in file(".")).settings(
  name := "zio-akka-http-tapir",
  scalacOptions += "-Ymacro-annotations",
  libraryDependencies ++= ZioDependencies.value ++
    AkkaDependencies.value ++
    TapirDependencies.value ++
    CirceDependencies.value,
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
)
