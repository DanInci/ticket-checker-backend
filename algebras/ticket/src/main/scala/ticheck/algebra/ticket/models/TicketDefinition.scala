package ticheck.algebra.ticket.models

import ticheck.TicketID
import ticheck.dao.ticket.{SoldTo, SoldToBirthday, SoldToTelephone}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class TicketDefinition(
  id:              TicketID,
  soldTo:          Option[SoldTo],
  soldToBirthday:  Option[SoldToBirthday],
  soldToTelephone: Option[SoldToTelephone],
)

object TicketDefinition {
  import ticheck.json._

  implicit val jsonCodec: Codec[TicketDefinition] = derive.codec[TicketDefinition]
}
