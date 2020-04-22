package ticheck.algebra.ticket.models

import ticheck.{OrganizationID, TicketID}
import ticheck.dao.ticket.models.TicketRecord
import ticheck.dao.ticket.{SoldAt, SoldTo, ValidatedAt}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class TicketList(
  id:             TicketID,
  organizationId: OrganizationID,
  soldTo:         Option[SoldTo],
  soldAt:         SoldAt,
  validatedAt:    Option[ValidatedAt],
)

object TicketList {
  import ticheck.json._

  implicit val jsonCodec: Codec[TicketList] = derive.codec[TicketList]

  def fromDAO(t: TicketRecord): TicketList =
    TicketList(
      t.id,
      t.organizationId,
      t.soldTo,
      t.soldAt,
      t.validatedAt,
    )
}
