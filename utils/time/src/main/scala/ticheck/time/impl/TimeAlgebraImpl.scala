package ticheck.time.impl

import java.time.OffsetDateTime

import ticheck.effect._
import ticheck.time.{TimeAlgebra, TimeConfig}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final private[time] class TimeAlgebraImpl private (timeConfig: TimeConfig) extends TimeAlgebra {

  override def now[F[_]: Sync]: F[OffsetDateTime] = Sync[F].delay(OffsetDateTime.now(timeConfig.zoneId))

}

private[time] object TimeAlgebraImpl {

  def sync[F[_]: Sync](timeConfig: TimeConfig): F[TimeAlgebra] =
    Sync[F].pure(new TimeAlgebraImpl(timeConfig))
}
