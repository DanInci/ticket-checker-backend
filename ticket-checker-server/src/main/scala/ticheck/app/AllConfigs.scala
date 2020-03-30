package ticheck.app

import ticheck.effect._
import ticheck.db.DBConfig
import ticheck.time.TimeConfig

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final case class AllConfigs(
  serverConfig: HttpServerConfig,
  dbConfig:     DBConfig,
  timeConfig:   TimeConfig,
)

object AllConfigs {

  def defaultR[F[_]: Sync]: Resource[F, AllConfigs] =
    for {
      serverConfig <- HttpServerConfig.fromNamespaceR[F]("ticket-checker.http-server")
      dbConfig     <- DBConfig.fromNamespaceR[F]("ticket-checker.database")
      timeConfig   <- TimeConfig.fromNamespaceR[F]("ticket-checker.time")
    } yield AllConfigs(
      serverConfig = serverConfig,
      dbConfig     = dbConfig,
      timeConfig   = timeConfig,
    )

}
