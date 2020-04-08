package ticheck.organizer.ticket.impl

import ticheck.{OrganizationID, PagingInfo, TicketID, UserID}
import ticheck.algebra.ticket.{TicketAlgebra, TicketCategory}
import ticheck.algebra.ticket.models.{Ticket, TicketDefinition, TicketList, TicketUpdateDefinition}
import ticheck.algebra.user.models.auth.UserAuthCtx
import ticheck.organizer.ticket.TicketOrganizer

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
private[ticket]final case class TicketOrganizerImpl[F[_]](private val ticketAlgebra: TicketAlgebra[F]) extends TicketOrganizer[F] {
  override def getOrganizationTickets(orgId: OrganizationID, pagingInfo: PagingInfo, byCategory: Option[TicketCategory], byUserId: Option[UserID], searchVal: Option[String])(implicit ctx: UserAuthCtx): F[List[TicketList]] = ???

  override def createTicket(orgId: OrganizationID, definition: TicketDefinition)(implicit ctx: UserAuthCtx): F[Ticket] = ???

  override def getTicket(orgId: OrganizationID, ticketId: TicketID)(implicit ctx: UserAuthCtx): F[Ticket] = ???

  override def updateTicket(orgId: OrganizationID, ticketId: TicketID, definition: TicketUpdateDefinition)(implicit ctx: UserAuthCtx): F[Ticket] = ???

  override def setTicketValidationStatus(orgId: OrganizationID, ticketID: TicketID, isValid: Boolean)(implicit ctx: UserAuthCtx): F[Ticket] = ???

  override def deleteTicket(orgId: OrganizationID, ticketId: TicketID)(implicit ctx: UserAuthCtx): F[Unit] = ???
}
