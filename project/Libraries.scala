import sbt._

/**
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 02/03/2020
  */
object Libraries {

  private lazy val pureharmVersion:    String = "0.0.5-RC2" //https://github.com/busymachines/pureharm/releases
  private lazy val pureharmAWSVersion: String = "0.0.4-M3"  //https://github.com/busymachines/pureharm-aws/releases
  private lazy val catsVersion:        String = "2.1.1"     //https://github.com/typelevel/cats/releases
  private lazy val catsEffectVersion:  String = "2.1.2"     //https://github.com/typelevel/cats-effect/releases
  private lazy val fs2Version:         String = "2.2.2"     //https://github.com/functional-streams-for-scala/fs2/releases
  private lazy val pureConfigVersion:  String = "0.12.3"    //https://github.com/pureconfig/pureconfig/releases
  private lazy val log4catsVersion:    String = "1.0.1"     //https://github.com/ChristopherDavenport/log4cats/releases
  private lazy val logbackVersion:     String = "1.2.3"     //java — https://github.com/qos-ch/logback/releases
  private lazy val doobieVersion:      String = "0.8.8"     //https://github.com/tpolecat/doobie/releases
  private lazy val http4sVersion:      String = "0.21.1"    //https://github.com/http4s/http4s/releases
  private lazy val fuuidVersion:       String = "0.3.0"     //https://github.com/ChristopherDavenport/fuuid/releases
  private lazy val flywayVersion:      String = "6.2.4"     //java — https://github.com/flyway/flyway/releases
  private lazy val postgresqlVersion:  String = "42.3.0"    //java — https://github.com/pgjdbc/pgjdbc/releases
  private lazy val hikariCPVersion:    String = "3.4.2"     //java — https://github.com/brettwooldridge/HikariCP/releases
  private lazy val prepyVersion:       String = "0.0.7"     //https://github.com/alexandrustana/prepy/releases
  private lazy val javaxMailVersion:   String = "1.6.2"     //https://github.com/javaee/javamail/releases
  private lazy val jwtVersion:         String = "4.2.0"     //https://github.com/pauldijou/jwt-scala/releases
  private lazy val bcryptVersion:      String = "4.1"       //https://github.com/t3hnar/scala-bcrypt/releases

  //=============================================================================
  //================================= TYPELEVEL =================================
  //=============================================================================

  //https://github.com/typelevel/cats/releases
  lazy val catsCore:    ModuleID = "org.typelevel" %% "cats-core"    % catsVersion withSources ()
  lazy val catsMacros:  ModuleID = "org.typelevel" %% "cats-macros"  % catsVersion withSources ()
  lazy val catsKernel:  ModuleID = "org.typelevel" %% "cats-kernel"  % catsVersion withSources ()
  lazy val catsLaws:    ModuleID = "org.typelevel" %% "cats-laws"    % catsVersion withSources ()
  lazy val catsTestkit: ModuleID = "org.typelevel" %% "cats-testkit" % catsVersion withSources ()

  lazy val cats: Seq[ModuleID] = Seq(
    catsCore,
    catsMacros,
    catsKernel,
    catsLaws,
    catsTestkit % Test,
  )

  //https://github.com/typelevel/cats-effect/releases
  lazy val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources ()

  //=============================================================================
  //================================= PUREHARM ==================================
  //=============================================================================

  //https://github.com/busymachines/pureharm/releases/
  def pureharm(m: String): ModuleID = "com.busymachines" %% s"pureharm-$m" % pureharmVersion

  lazy val pureharmCore:             ModuleID = pureharm("core")              withSources ()
  lazy val pureharmCoreAnomaly:      ModuleID = pureharm("core-anomaly")      withSources ()
  lazy val pureharmCorePhantom:      ModuleID = pureharm("core-phantom")      withSources ()
  lazy val pureharmCoreIdentifiable: ModuleID = pureharm("core-identifiable") withSources ()
  lazy val pureharmEffectsCats:      ModuleID = pureharm("effects-cats")      withSources ()
  lazy val pureharmJsonCirce:        ModuleID = pureharm("json-circe")        withSources ()
  lazy val pureharmConfig:           ModuleID = pureharm("config")            withSources ()

  lazy val pureharmDBCore:       ModuleID = pureharm("db-core")        withSources ()
  lazy val pureharmDBCoreFlyway: ModuleID = pureharm("db-core-flyway") withSources ()

  lazy val pureharmAll: Seq[ModuleID] =
    Seq(
      pureharmCoreAnomaly,
      pureharmCoreIdentifiable,
      pureharmCorePhantom,
      pureharmCore,
      pureharmEffectsCats,
      pureharmJsonCirce,
      pureharmConfig,
      pureharmDBCore,
      pureharmDBCoreFlyway,
    )

  //https://github.com/busymachines/pureharm-aws/releases/
  def pureharmAWS(m: String): ModuleID = "com.busymachines" %% s"pureharm-aws-$m" % pureharmAWSVersion
  lazy val pureharmAWSCore:       ModuleID = pureharmAWS("core")       withSources ()
  lazy val pureharmAWSS3:         ModuleID = pureharmAWS("s3")         withSources ()
  lazy val pureharmAWSCloudfront: ModuleID = pureharmAWS("cloudfront") withSources ()
  lazy val pureharmAWSLogger:     ModuleID = pureharmAWS("logger")     withSources ()

  //https://github.com/http4s/http4s/releases
  lazy val http4sBlazeServer: ModuleID = "org.http4s" %% "http4s-blaze-server" % http4sVersion withSources ()
  lazy val http4sCirce:       ModuleID = "org.http4s" %% "http4s-circe"        % http4sVersion withSources ()
  lazy val http4sDSL:         ModuleID = "org.http4s" %% "http4s-dsl"          % http4sVersion withSources ()
  lazy val http4sClient:      ModuleID = "org.http4s" %% "http4s-blaze-client" % http4sVersion withSources ()

  lazy val http4s: Seq[ModuleID] = Seq(
    http4sBlazeServer,
    http4sCirce,
    http4sDSL,
    fuuidHttp4s,
    fuuidCirce,
  )

  //https://github.com/functional-streams-for-scala/fs2
  lazy val fs2:   ModuleID = "co.fs2" %% "fs2-core" % fs2Version withSources ()
  lazy val fs2io: ModuleID = "co.fs2" %% "fs2-io"   % fs2Version withSources ()

  //https://github.com/tpolecat/doobie
  lazy val doobieCore:     ModuleID = "org.tpolecat" %% "doobie-core"     % doobieVersion withSources ()
  lazy val doobieHikari:   ModuleID = "org.tpolecat" %% "doobie-hikari"   % doobieVersion withSources ()
  lazy val doobiePostgres: ModuleID = "org.tpolecat" %% "doobie-postgres" % doobieVersion withSources ()
  lazy val doobieTK:       ModuleID = "org.tpolecat" %% "doobie-specs2"   % doobieVersion % Test withSources ()

  lazy val doobie: Seq[ModuleID] = Seq(doobieCore, doobieHikari, doobiePostgres, doobieTK)

  lazy val postgresql: ModuleID = "org.postgresql"            % "postgresql"  % postgresqlVersion withSources ()
  lazy val hikariCP:   ModuleID = "com.zaxxer"                % "HikariCP"    % hikariCPVersion   withSources ()
  lazy val flyway:     ModuleID = "org.flywaydb"              % "flyway-core" % flywayVersion     withSources ()
  lazy val prepy:      ModuleID = "com.github.alexandrustana" %% "prepy"      % prepyVersion      withSources ()

  //============================================================================================

  //https://github.com/pureconfig/pureconfig/releases
  lazy val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % pureConfigVersion withSources ()

  //============================================================================================
  //=========================================  logging =========================================
  //============================================================================================

  //https://github.com/ChristopherDavenport/log4cats/releases
  lazy val log4cats: ModuleID = "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion withSources ()

  //it is the backend implementation used by log4cats/releases
  lazy val logbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % logbackVersion withSources ()

  //============================================================================================
  //========================================== MISC ============================================
  //============================================================================================

  //https://github.com/javaee/javamail/releases
  lazy val javaxMail = "com.sun.mail" % "javax.mail" % javaxMailVersion withSources ()

  //https://github.com/ChristopherDavenport/fuuid/releases
  lazy val fuuid       = "io.chrisdavenport" %% "fuuid"        % fuuidVersion withSources ()
  lazy val fuuidCirce  = "io.chrisdavenport" %% "fuuid-circe"  % fuuidVersion withSources ()
  lazy val fuuidHttp4s = "io.chrisdavenport" %% "fuuid-http4s" % fuuidVersion withSources ()

  //============================================================================================
  //=================================== JWT & CRYPTO ===========================================
  //============================================================================================

  lazy val jwtCore  = "com.pauldijou" %% "jwt-core"  % jwtVersion withSources ()
  lazy val jwtCirce = "com.pauldijou" %% "jwt-circe" % jwtVersion withSources ()

  lazy val bcrypt = "com.github.t3hnar" %% "scala-bcrypt" % bcryptVersion withSources ()

  lazy val jwtPlusCrypto: Seq[ModuleID] = Seq(
    jwtCore,
    jwtCirce,
    bcrypt,
  )

}
