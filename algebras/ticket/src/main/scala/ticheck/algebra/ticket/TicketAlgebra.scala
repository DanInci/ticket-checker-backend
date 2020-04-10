package ticheck.algebra.ticket

import ticheck.{OrganizationID, PagingInfo, TicketID, UserID}
import ticheck.algebra.ticket.models._
import ticheck.dao.ticket.TicketCategory

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait TicketAlgebra[F[_]] {

  def getAll(
    organizationId: OrganizationID,
    pagingInfo:     PagingInfo,
    byCategory:     Option[TicketCategory],
    byUserId:       Option[UserID],
    searchVal:      Option[String],
  ): F[List[TicketList]]

  def create(organizationId: OrganizationID, definition: TicketDefinition): F[Ticket]

  def getById(organizationId: OrganizationID, ticketId: TicketID): F[Ticket]

  def updateById(organizationId: OrganizationID, ticketId: TicketID, definition: TicketUpdateDefinition): F[Ticket]

  def setValidationStatusById(organizationId: OrganizationID, ticketId: TicketID, isValidated: IsValidated): F[Ticket]

  def deleteById(organizationId: OrganizationID, ticketId: TicketID): F[Unit]

}
