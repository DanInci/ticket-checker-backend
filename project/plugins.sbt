//https://github.com/xerial/sbt-pack/releases
addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.12")

/**
  *
  * neat way of visualizing the dependency graph both in the sbt repl, and to export
  * it as an .svg
  */
//https://github.com/jrudolph/sbt-dependency-graph/releases
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

/**
  * Provides various code statistics for your code
  */
//https://github.com/orrsella/sbt-stats/releases
addSbtPlugin("com.orrsella" %% "sbt-stats" % "1.0.7")

//https://github.com/scalameta/sbt-scalafmt/releases
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.2")
