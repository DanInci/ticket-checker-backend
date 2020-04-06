package ticheck.dao.ticket.models

import ticheck._
import ticheck.dao.ticket._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
final case class TicketRecord(
  id:              TicketID,
  organizationId:  OrganizationID,
  soldTo:          Option[SoldTo],
  soldToBirthday:  Option[SoldToBirthday],
  soldToTelephone: Option[SoldToTelephone],
  soldBy:          Option[SoldBy],
  soldByName:      SoldByName,
  soldAt:          SoldAt,
  validatedBy:     Option[ValidatedBy],
  validatedByName: Option[ValidatedByName],
  validatedAt:     Option[ValidatedAt],
)
