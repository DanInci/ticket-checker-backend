package ticheck.time

import ticheck.effect._
import ticheck.config._

import java.time.ZoneId

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final case class TimeConfig(
  zoneId: ZoneId,
)

object TimeConfig extends ConfigLoader[TimeConfig] {
  implicit override val configReader: ConfigReader[TimeConfig] = semiauto.deriveReader[TimeConfig]

  override def default[F[_]: Sync]: F[TimeConfig] =
    this.load[F]("ticket-checker.time")
}
