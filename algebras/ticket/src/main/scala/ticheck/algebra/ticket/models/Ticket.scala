package ticheck.algebra.ticket.models

import ticheck.algebra.user.models.UserProfile
import ticheck.dao.ticket._
import ticheck.dao.ticket.models.TicketRecord
import ticheck.dao.user.models.UserRecord
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
  validatedByName: Option[ValidatedByName],
  validatedAt:     Option[ValidatedAt],
)

object Ticket {
  import ticheck.json._

  implicit val jsonCodec: Codec[Ticket] = derive.codec[Ticket]

  def fromDAO(t: TicketRecord, soldBy: Option[UserRecord], validatedBy: Option[UserRecord]): Ticket =
    Ticket(
      t.id,
      t.organizationId,
      t.soldTo,
      t.soldToBirthday,
      t.soldToTelephone,
      soldBy.map(UserProfile.fromDAO),
      if (soldBy.isDefined) None else Some(t.soldByName),
      t.soldAt,
      validatedBy.map(UserProfile.fromDAO),
      if (validatedBy.isDefined) None else t.validatedByName,
      t.validatedAt,
    )

}
