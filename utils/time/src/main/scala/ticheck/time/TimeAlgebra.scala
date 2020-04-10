package ticheck.time

import java.sql.Timestamp

import ticheck.effect._
import java.time.{LocalDate, LocalDateTime, OffsetDateTime}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait TimeAlgebra {

  def now[F[_]: Sync]: F[OffsetDateTime]

  def toLocalDate(timestamp: Timestamp): LocalDate

  def toOffsetDateTime(dateTime: LocalDateTime): OffsetDateTime

  def toOffsetDateTime(timestamp: Timestamp): OffsetDateTime

  def toTimestamp(date: LocalDate): Timestamp

  def toTimestamp(date: OffsetDateTime): Timestamp

}
