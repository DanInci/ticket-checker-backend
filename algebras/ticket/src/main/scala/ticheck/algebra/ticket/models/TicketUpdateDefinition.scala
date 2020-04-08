package ticheck.algebra.ticket.models

import ticheck.dao.ticket.{SoldTo, SoldToBirthday, SoldToTelephone}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class TicketUpdateDefinition(
  soldTo:          Option[SoldTo],
  soldToBirthday:  Option[SoldToBirthday],
  soldToTelephone: Option[SoldToTelephone],
)

object TicketUpdateDefinition {
  import ticheck.json._

  implicit val jsonCodec: Codec[TicketUpdateDefinition] = derive.codec[TicketUpdateDefinition]
}
