package ticheck.algebra.ticket

import ticheck.effect._
import ticheck.json.{Codec, Decoder, Encoder}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
sealed trait IntervalType {
  def asString: String
}

object IntervalType {

  implicit def intervalTypeCodec: Codec[IntervalType] = Codec.from(intervalTypeDecoder, intervalTypeEncoder)

  private val intervalTypeEncoder: Encoder[IntervalType] = Encoder.apply[String].contramap(_.asString)
  private val intervalTypeDecoder: Decoder[IntervalType] =
    Decoder.apply[String].emap(s => IntervalType.fromString(s).leftMap(_.getMessage))

  private val HourlyString = "HOURLY"
  private val DailyString  = "DAILY"
  private val WeeklyString = "WEEKYL"

  final object HourlyInterval extends IntervalType {
    override def asString: String = HourlyString
  }
  final object DailyInterval extends IntervalType {
    override def asString: String = DailyString
  }
  final object WeeklyInterval extends IntervalType {
    override def asString: String = WeeklyString
  }

  private val intervalsMap: Map[String, IntervalType] =
    Map(
      HourlyString -> HourlyInterval,
      DailyString  -> DailyInterval,
      WeeklyString -> WeeklyInterval,
    )

  def fromString(typeString: String): Attempt[IntervalType] = intervalsMap.get(typeString) match {
    case None    => Attempt.raiseError(InvalidIntervalTypeAnomaly(typeString))
    case Some(r) => Attempt.pure(r)
  }

  def unsafe(s: String): IntervalType = this.fromString(s) match {
    case Left(e)      => throw e
    case Right(value) => value
  }

}
