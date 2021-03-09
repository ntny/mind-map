import Dependencies._

ThisBuild / organization := "com.ntny"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.4"

lazy val mind_map = (project in file("."))
  .aggregate(web, dba)

lazy val web = (project in file("web"))
  .settings(
    libraryDependencies ++= Seq(
      org.http4s      %% "http4s-blaze-server" % ver.http4s,
      org.http4s      %% "http4s-blaze-client" % ver.http4s,
      org.http4s      %% "http4s-circe"        % ver.http4s,
      org.http4s      %% "http4s-dsl"          % ver.http4s,
      org.circle       %% "circe-generic"       % ver.circe,
      org.circle        %% "circe-refined"       % ver.circe,
      "org.scalameta"   %% "munit"               % ver.munit          % Test,
      "org.typelevel"   %% "munit-cats-effect-2" % ver.munitCatsEffect % Test,
      "ch.qos.logback"  %  "logback-classic"     % ver.logback,
      org.timepit      %% "refined"             % ver.refined,
      "org.scalameta"   %% "svm-subs"            % "20.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  ).dependsOn(dba)

lazy val dba = (project in file("dba"))
  .enablePlugins(FlywayPlugin)
  .settings(
      flywayUrl := "jdbc:postgresql://localhost:5432/postgres",
      flywayUser := "postgres",
      flywayPassword := "password123",
      flywayLocations += "migrations",
  libraryDependencies ++= Seq(
    org.tpolecat %% "doobie-core"      % ver.doobie,

    org.tpolecat %% "doobie-h2"        % ver.doobie,          // H2 driver 1.4.200 + type mappings.
    org.tpolecat %% "doobie-hikari"    % ver.doobie,          // HikariCP transactor.
    org.tpolecat %% "doobie-postgres"  % ver.doobie,          // Postgres driver 42.2.12 + type mappings.
    org.tpolecat %% "doobie-quill"     % ver.doobie,          // Support for Quill 3.5.1
    org.tpolecat %% "doobie-specs2"    % ver.doobie % "test", // Specs2 support for typechecking statements.
    org.tpolecat %% "doobie-scalatest" % ver.doobie % "test",  // ScalaTest support for typechecking statements.
    org.timepit  %% "refined"          % ver.refined,
  )
)
