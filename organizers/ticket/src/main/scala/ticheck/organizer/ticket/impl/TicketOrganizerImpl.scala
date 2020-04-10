package ticheck.organizer.ticket.impl

import ticheck.{OrganizationID, PagingInfo, TicketID, UserID}
import ticheck.algebra.ticket.{IsValidated, TicketAlgebra}
import ticheck.algebra.ticket.models.{Ticket, TicketDefinition, TicketList, TicketUpdateDefinition}
import ticheck.algebra.user.UserAlgebra
import ticheck.auth.models.UserAuthCtx
import ticheck.dao.ticket.TicketCategory
import ticheck.organizer.ticket.TicketOrganizer
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
private[ticket]final case class TicketOrganizerImpl[F[_]](
   private val userAlgebra: UserAlgebra[F],
   private val ticketAlgebra: TicketAlgebra[F]
)(implicit F: Sync[F]) extends TicketOrganizer[F] {

  override def getOrganizationTickets(orgId: OrganizationID, pagingInfo: PagingInfo, byCategory: Option[TicketCategory], byUserId: Option[UserID], searchVal: Option[String])(implicit ctx: UserAuthCtx): F[List[TicketList]] =
    for {
      tickets <- ticketAlgebra.getAllForOrganization(
        organizationId = orgId,
        pagingInfo = pagingInfo,
        byCategory = byCategory,
        byUserId = byUserId,
        searchVal = searchVal
      )
    } yield tickets

  override def createTicket(orgId: OrganizationID, definition: TicketDefinition)(implicit ctx: UserAuthCtx): F[Ticket] =
    for {
      ticket <- ticketAlgebra.create(orgId, definition)(ctx.userId, ctx.name)
    } yield ticket

  override def getTicket(orgId: OrganizationID, ticketId: TicketID)(implicit ctx: UserAuthCtx): F[Ticket] =
    for {
      ticket <- ticketAlgebra.getById(orgId, ticketId)
    } yield ticket

  override def updateTicket(orgId: OrganizationID, ticketId: TicketID, definition: TicketUpdateDefinition)(implicit ctx: UserAuthCtx): F[Ticket] =
    for {
      ticket <- ticketAlgebra.updateById(orgId, ticketId, definition)
    } yield ticket

  override def setTicketValidationStatus(orgId: OrganizationID, ticketId: TicketID, isValidated: IsValidated)(implicit ctx: UserAuthCtx): F[Ticket] =
    for {
      ticket <- ticketAlgebra.setValidationStatusById(orgId, ticketId, isValidated)(ctx.userId, ctx.name)
    } yield ticket

  override def deleteTicket(orgId: OrganizationID, ticketId: TicketID)(implicit ctx: UserAuthCtx): F[Unit] =
    for {
      _ <- ticketAlgebra.deleteById(orgId, ticketId)
    } yield ()

}
