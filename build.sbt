def rebuildAlias(p: String): String =
  s";$p/clean;$p/update;$p/compile;$p/test:compile"

def testAlias(p: String): String =
  s"${rebuildAlias(p)};$p/test"

addCommandAlias("rebuild", rebuildAlias("ticket-checker-server"))
addCommandAlias("test", testAlias("ticket-checker-server"))
addCommandAlias("mkJar", "ticket-checker-server/pack")
addCommandAlias("cleanJar", ";rebuild;mkJar")

//============================================================================================
//=======================================  projects ==========================================
//============================================================================================

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "ticket-checker-backend",
  )
  .aggregate(
    `ticket-checker-server`,
  )

lazy val `ticket-checker-server` = Project(s"ticket-checker-server", file("ticket-checker-server"))
  .enablePlugins(PackPlugin)
  .settings(commonSettings)
  .settings(serverPackSettings("ticket-checker-server" -> "com.ticket.checker.TicketCheckerApp"))
  .dependsOn(
    `ticket-checker-rest`,
    `organizer-organization`,
    `organizer-user`,
    `organizer-ticket`,
    `algebra-organization`,
    `algebra-user`,
    `algebra-ticket`,
    `algebra-auth`,
    `algebra-auth-http`,
    `dao-organization`,
    `dao-user`,
    `dao-ticket`,
    `util-core`,
    `util-db`,
    `util-http`,
    `util-time`,
  )
  .aggregate(
    `ticket-checker-rest`,
    `organizer-organization`,
    `organizer-user`,
    `organizer-ticket`,
    `algebra-organization`,
    `algebra-user`,
    `algebra-ticket`,
    `algebra-auth`,
    `algebra-auth-http`,
    `dao-organization`,
    `dao-user`,
    `dao-ticket`,
    `util-core`,
    `util-db`,
    `util-http`,
    `util-time`,
  )

lazy val `ticket-checker-rest` = Project(s"ticket-checker-rest", file(s"ticket-checker-rest"))
  .settings(commonSettings)
  .dependsOn(
    `organizer-organization`,
    `organizer-user`,
    `organizer-ticket`,
    `algebra-auth-http`
  )
  .aggregate(
    `organizer-organization`,
    `organizer-user`,
    `organizer-ticket`,
    `algebra-auth-http`
  )

//********************************************************************************************
//********************************************************************************************
//*************************************** organizers *****************************************
//********************************************************************************************
//********************************************************************************************

def organizerModule(name: String): Project = Project(s"organizer-$name", file(s"organizers/$name"))

lazy val `organizer-user` = organizerModule("user")
  .settings(commonSettings)
  .dependsOn(
    `algebra-user`,
    `util-core`,
    `util-http`,
  )
  .aggregate(
    `algebra-user`,
    `util-core`,
    `util-http`,
  )

lazy val `organizer-ticket` = organizerModule("ticket")
  .settings(commonSettings)
  .dependsOn(
    `algebra-ticket`,
    `util-core`,
    `util-http`,
  )
  .aggregate(
    `algebra-ticket`,
    `util-core`,
    `util-http`,
  )

lazy val `organizer-organization` = organizerModule("organization")
  .settings(commonSettings)
  .dependsOn(
    `algebra-organization`,
    `util-core`,
    `util-http`,
  )
  .aggregate(
    `algebra-organization`,
    `util-core`,
    `util-http`,
  )

//********************************************************************************************
//********************************************************************************************
//***************************************** algebras *****************************************
//********************************************************************************************
//********************************************************************************************

def algebraModule(name: String): Project = Project(s"algebra-$name", file(s"algebras/$name"))

lazy val `algebra-auth` = algebraModule("auth")
  .settings(commonSettings)
  .dependsOn(
    `util-core`,
  )
  .aggregate(
    `util-core`,
  )
  .settings(
    libraryDependencies ++= Libraries.tsecJWT
  )

lazy val `algebra-auth-http` = algebraModule("auth-http")
  .settings(commonSettings)
  .dependsOn(
    `util-http`,
    `algebra-auth`
  )
  .aggregate(
    `util-http`,
    `algebra-auth`
  )

lazy val `algebra-ticket` = algebraModule("ticket")
  .settings(commonSettings)
  .dependsOn(
    `dao-ticket`,
    `util-core`,
  )
  .aggregate(
    `dao-ticket`,
    `util-core`,
  )

lazy val `algebra-user` = algebraModule("user")
  .settings(commonSettings)
  .dependsOn(
    `dao-user`,
    `util-core`,
  )
  .aggregate(
    `dao-user`,
    `util-core`,
  )

lazy val `algebra-organization` = algebraModule("organization")
  .settings(commonSettings)
  .dependsOn(
    `dao-organization`,
    `util-core`,
  )
  .aggregate(
    `dao-organization`,
    `util-core`,
  )

//********************************************************************************************
//********************************************************************************************
//******************************************* daos ******************************************
//********************************************************************************************
//********************************************************************************************

def daoModule(name: String): Project = Project(s"dao-$name", file(s"daos/$name"))

lazy val `dao-ticket` = daoModule("ticket")
  .settings(commonSettings)
  .dependsOn(
    `util-core`,
    `util-db`,
    `util-time`
  )
  .aggregate(
    `util-core`,
    `util-db`,
    `util-time`
  )

lazy val `dao-user` = daoModule("user")
  .settings(commonSettings)
  .dependsOn(
    `util-core`,
    `util-db`,
    `util-time`
  )
  .aggregate(
    `util-core`,
    `util-db`,
    `util-time`
  )

lazy val `dao-organization` = daoModule("organization")
  .settings(commonSettings)
  .dependsOn(
    `util-core`,
    `util-db`,
    `util-time`
  )
  .aggregate(
    `util-core`,
    `util-db`,
    `util-time`
  )


//********************************************************************************************
//********************************************************************************************
//******************************************* utils ******************************************
//********************************************************************************************
//********************************************************************************************

def utilModule(name: String): Project = Project(s"util-$name", file(s"utils/$name"))

lazy val `util-core` = utilModule("core")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.pureharmCore,
      Libraries.pureharmEffectsCats,
      Libraries.pureharmJsonCirce,
      Libraries.pureharmConfig,
      Libraries.log4cats,
      Libraries.logbackClassic,
      Libraries.fuuid, //It's OK since it's a cats-effect wrapper over java.util.UUID
      Libraries.fuuidCirce,
      Libraries.fs2,
    ),
  )

lazy val `util-db` = utilModule("db")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Libraries.doobie ++ Seq(
      Libraries.pureharmDBCore,
      Libraries.pureharmDBCoreFlyway,
      Libraries.prepy
    ),
  )
  .dependsOn(
    `util-core`,
  )
  .aggregate(
    `util-core`,
  )

lazy val `util-http` = utilModule("http")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Libraries.http4s ++ Seq(
      Libraries.fuuidHttp4s
    ),
  )
  .dependsOn(
    `util-core`,
  )
  .aggregate(
    `util-core`,
  )

lazy val `util-time` = utilModule("time")
  .settings(commonSettings)
  .dependsOn(
    `util-core`,
  )
  .aggregate(
    `util-core`,
  )

//============================================================================================
//====================================  commons settings =====================================
//============================================================================================

def commonSettings: Seq[Setting[_]] = CompilerSettings.compilerSettings ++ Seq(
  organization := "ro.elementum",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),  //grab newest pureharm builds without waiting a lot for maven central
    Resolver.sonatypeRepo("snapshots"), //currently used for monix-catnap releases which compile on 2.13
  ),
)

//============================================================================================
//=======================================  pack settings =====================================
//============================================================================================

def serverPackSettings(main: (String, String)): Seq[Setting[_]] = Seq(
  packJarNameConvention                := "original",
  packCopyDependenciesUseSymbolicLinks := false,
  packMain                             := Map(main),
)

/**
  * See SBT docs:
  * https://www.scala-sbt.org/release/docs/Multi-Project.html#Per-configuration+classpath+dependencies
  *
  * or an example:
  * {{{
  * val testModule = project
  *
  * val prodModule = project
  *   .dependsOn(asTestingLibrary(testModule))
  * }}}
  * To ensure that testing code and dependencies
  * do not wind up in the "compile" (i.e.) prod part of your
  * application.
  */
def asTestingLibrary(p: Project): ClasspathDependency = p % "test -> compile"

/**
  * Will have to clean this up eventually, but basically, `shut-testkit`,
  * is a full fledged stand alone library. While `shut-email` just provides
  * some test helpers in it `test/scala` definitions. Ideally, we'd create
  * a `shut-email-testkit` module, but sometimes that's overkill.
  *
  * Example:
  * {{{
  *   val server = project
  *     .dependsOn(withTestSharing(`shut-email`))
  * }}}
  *
  */
def withTestSharing(p: Project): ClasspathDependency = p % "test -> test"
