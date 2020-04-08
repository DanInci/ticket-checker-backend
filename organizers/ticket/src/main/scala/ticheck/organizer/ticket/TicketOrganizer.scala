package ticheck.organizer.ticket

import ticheck.{OrganizationID, PagingInfo, TicketID, UserID}
import ticheck.algebra.ticket.{TicketAlgebra, TicketCategory}
import ticheck.algebra.ticket.models._
import ticheck.auth.models.UserAuthCtx
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait TicketOrganizer[F[_]] {

  def getOrganizationTickets(
    orgId:        OrganizationID,
    pagingInfo:   PagingInfo,
    byCategory:   Option[TicketCategory] = None,
    byUserId:     Option[UserID] = None,
    searchVal:    Option[String] = None,
  )(implicit ctx: UserAuthCtx): F[List[TicketList]]

  def createTicket(orgId: OrganizationID, definition: TicketDefinition)(implicit ctx: UserAuthCtx): F[Ticket]

  def getTicket(orgId: OrganizationID, ticketId: TicketID)(implicit ctx: UserAuthCtx): F[Ticket]

  def updateTicket(orgId: OrganizationID, ticketId: TicketID, definition: TicketUpdateDefinition)(
    implicit ctx:         UserAuthCtx,
  ): F[Ticket]

  def setTicketValidationStatus(orgId: OrganizationID, ticketID: TicketID, isValid: Boolean)(
    implicit ctx:                      UserAuthCtx,
  ): F[Ticket]

  def deleteTicket(orgId: OrganizationID, ticketId: TicketID)(implicit ctx: UserAuthCtx): F[Unit]

}

object TicketOrganizer {

  def apply[F[_]: Sync](ticketAlgebra: TicketAlgebra[F]): F[TicketOrganizer[F]] = Sync[F].pure(
    new impl.TicketOrganizerImpl[F](ticketAlgebra),
  )

}
