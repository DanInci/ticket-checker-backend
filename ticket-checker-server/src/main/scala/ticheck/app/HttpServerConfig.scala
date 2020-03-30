package ticheck.app

import ticheck.effect.Sync
import ticheck.config._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final case class HttpServerConfig(
  port:    Int,
  host:    String,
  apiRoot: String,
)

object HttpServerConfig extends ConfigLoader[HttpServerConfig] {
  implicit override val configReader: ConfigReader[HttpServerConfig] = semiauto.deriveReader[HttpServerConfig]

  override def default[F[_]: Sync]: F[HttpServerConfig] =
    this.load[F]("ticket-checker.server")
}
