package ticheck.algebra.ticket.impl

import ticheck.{OrganizationID, PagingInfo, TicketID, UserID}
import ticheck.algebra.ticket.models.{Ticket, TicketDefinition, TicketList, TicketStatistic, TicketUpdateDefinition}
import ticheck.algebra.ticket.{
  IntervalType,
  IsValidated,
  StatisticsSize,
  StatisticsTimestamp,
  TicketAlgebra,
  TicketCategory,
  TicketModuleAlgebra,
  TicketStatisticsAlgebra,
}
import ticheck.dao.ticket.TicketSQL
import ticheck.effect._
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[ticket] class TicketAlgebraImpl[F[_]] private (
  ticketSQL:  TicketSQL[ConnectionIO],
)(implicit F: Async[F], transactor: Transactor[F])
    extends TicketAlgebra[F] with TicketStatisticsAlgebra[F] with DBOperationsAlgebra[F] {

  override def getAll(
    organizationId: OrganizationID,
    pagingInfo:     PagingInfo,
    byCategory:     Option[TicketCategory],
    byUserId:       Option[UserID],
    searchVal:      Option[String],
  ): F[List[TicketList]] = ???

  override def create(organizationId: OrganizationID, definition: TicketDefinition): F[Ticket] = ???

  override def getById(organizationId: OrganizationID, ticketId: TicketID): F[Ticket] = ???

  override def updateById(
    organizationId: OrganizationID,
    ticketId:       TicketID,
    definition:     TicketUpdateDefinition,
  ): F[Ticket] = ???

  override def setValidationStatusById(
    organizationId: OrganizationID,
    ticketId:       TicketID,
    isValidated:    IsValidated,
  ): F[Ticket] = ???

  override def deleteById(organizationId: OrganizationID, ticketId: TicketID): F[Unit] = ???

  override def getCountStats(
    organizationId: OrganizationID,
    byCategory:     TicketCategory,
    byInterval:     IntervalType,
    howMany:        StatisticsSize,
    until:          StatisticsTimestamp,
  ): F[List[TicketStatistic]] = ???

}

private[ticket] object TicketAlgebraImpl {

  def async[F[_]: Async: Transactor](ticketSQL: TicketSQL[ConnectionIO]): F[TicketModuleAlgebra[F]] =
    Async[F].pure(new TicketAlgebraImpl[F](ticketSQL))

}
