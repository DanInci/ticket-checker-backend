package ticheck.algebra.ticket

import ticheck.effect._
import ticheck.json._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
sealed trait TicketCategory {
  def asString: String
}

object TicketCategory {

  implicit def ticketCategoryCodec: Codec[TicketCategory] = Codec.from(ticketCategoryDecoder, ticketCategoryEncoder)

  private val ticketCategoryEncoder: Encoder[TicketCategory] = Encoder.apply[String].contramap(_.asString)
  private val ticketCategoryDecoder: Decoder[TicketCategory] =
    Decoder.apply[String].emap(s => TicketCategory.fromString(s).leftMap(_.getMessage))

  private val SoldString      = "SOLD"
  private val ValidatedString = "VALIDATED"

  final object SoldTicket extends TicketCategory {
    override def asString: String = SoldString
  }
  final object ValidatedTicket extends TicketCategory {
    override def asString: String = ValidatedString
  }
  private val categoryMap: Map[String, TicketCategory] =
    Map(
      SoldString      -> SoldTicket,
      ValidatedString -> ValidatedTicket,
    )

  def fromString(categoryString: String): Attempt[TicketCategory] = categoryMap.get(categoryString) match {
    case None    => Attempt.raiseError(InvalidTicketCategoryAnomaly(categoryString))
    case Some(r) => Attempt.pure(r)
  }

  def unsafe(s: String): TicketCategory = this.fromString(s) match {
    case Left(e)      => throw e
    case Right(value) => value
  }

}
