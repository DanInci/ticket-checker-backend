package ticheck.algebra.ticket.models

import ticheck.algebra.user.models.UserProfile
import ticheck.dao.ticket._
import ticheck.{OrganizationID, TicketID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class Ticket(
  id:              TicketID,
  organizationId:  OrganizationID,
  soldTo:          Option[SoldTo],
  soldToBirthday:  Option[SoldToBirthday],
  soldToTelephone: Option[SoldToTelephone],
  soldBy:          Option[UserProfile],
  soldByName:      Option[SoldByName],
  soldAt:          SoldAt,
  validatedBy:     Option[UserProfile],
  validatedByName: Option[SoldByName],
  validatedAt:     ValidatedAt,
)

object Ticket {
  import ticheck.json._

  implicit val jsonCodec: Codec[Ticket] = derive.codec[Ticket]
}
