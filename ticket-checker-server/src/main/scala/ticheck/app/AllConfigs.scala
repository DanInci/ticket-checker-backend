package ticheck.app

import ticheck.auth.JWTAuthConfig
import ticheck.effect._
import ticheck.db.DBConfig
import ticheck.email.EmailConfig
import ticheck.time.TimeConfig

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final case class AllConfigs(
  serverConfig:  HttpServerConfig,
  dbConfig:      DBConfig,
  emailConfig:   EmailConfig,
  timeConfig:    TimeConfig,
  jwtAuthConfig: JWTAuthConfig,
)

object AllConfigs {

  def defaultR[F[_]: Sync]: Resource[F, AllConfigs] =
    for {
      serverConfig  <- HttpServerConfig.fromNamespaceR[F]("ticket-checker.http-server")
      dbConfig      <- DBConfig.fromNamespaceR[F]("ticket-checker.database")
      emailConfig   <- EmailConfig.fromNamespaceR[F]("ticket-checker.email")
      timeConfig    <- TimeConfig.fromNamespaceR[F]("ticket-checker.time")
      jwtAuthConfig <- JWTAuthConfig.fromNamespaceR[F]("ticket-checker.jwt")
    } yield AllConfigs(
      serverConfig  = serverConfig,
      dbConfig      = dbConfig,
      emailConfig   = emailConfig,
      timeConfig    = timeConfig,
      jwtAuthConfig = jwtAuthConfig,
    )

}
