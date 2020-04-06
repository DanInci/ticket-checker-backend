package ticheck.time.impl

import java.sql.Timestamp
import java.time.{LocalDate, OffsetDateTime}

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

  override def toOffsetDateTime(timestamp: Timestamp): OffsetDateTime =
    timestamp.toLocalDateTime.atZone(timeConfig.zoneId).toOffsetDateTime

  override def toTimestamp(date: OffsetDateTime): Timestamp =
    Timestamp.valueOf(date.atZoneSameInstant(timeConfig.zoneId).toLocalDateTime)

  override def toLocalDate(timestamp: Timestamp): LocalDate =
    timestamp.toLocalDateTime.toLocalDate

  override def toTimestamp(date: LocalDate): Timestamp =
    Timestamp.valueOf(date.atStartOfDay)
}

private[time] object TimeAlgebraImpl {

  def sync[F[_]: Sync](timeConfig: TimeConfig): F[TimeAlgebra] =
    Sync[F].pure(new TimeAlgebraImpl(timeConfig))
}
