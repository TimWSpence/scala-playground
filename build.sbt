import Dependencies._

lazy val root = (project in file("."))
  .settings(
    organization := "org.timspence",
    name := "scala-playground",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.http4s"                 %% "http4s-blaze-server" % Versions.http4s,
      "org.http4s"                 %% "http4s-circe"        % Versions.http4s,
      "org.http4s"                 %% "http4s-dsl"          % Versions.http4s,
      "org.typelevel"              %% "cats-core"           % Versions.cats,
      "org.typelevel"              %% "cats-effect"         % Versions.catsEffect,
      "com.github.julien-truffaut" %%  "monocle-core"       % Versions.monocle,
      "com.github.julien-truffaut" %%  "monocle-macro"      % Versions.monocle
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.9"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.0-M4")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
