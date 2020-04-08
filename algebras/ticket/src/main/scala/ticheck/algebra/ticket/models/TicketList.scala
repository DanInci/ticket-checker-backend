package ticheck.algebra.ticket.models

import ticheck.TicketID
import ticheck.dao.ticket.{SoldAt, ValidatedAt}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class TicketList(
  id:          TicketID,
  soldAt:      SoldAt,
  validatedAt: ValidatedAt,
)

object TicketList {
  import ticheck.json._

  implicit val jsonCodec: Codec[TicketList] = derive.codec[TicketList]
}
