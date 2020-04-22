package ticheck.organizer.statistic.impl

import java.time.LocalDateTime

import ticheck.algebra.organization.OrganizationStatisticsAlgebra
import ticheck.{Count, OrganizationID}
import ticheck.algebra.ticket.models.TicketStatistic
import ticheck.algebra.ticket._
import ticheck.auth.models.UserAuthCtx
import ticheck.dao.organization.membership.OrganizationRole
import ticheck.dao.ticket.TicketCategory
import ticheck.organizer.statistic.StatisticOrganizer
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[statistic] case class StatisticOrganizerImpl[F[_]](
  private val organizationStatisticsAlgebra: OrganizationStatisticsAlgebra[F],
  private val ticketStatisticsAlgebra:       TicketStatisticsAlgebra[F],
)(implicit F:                                Sync[F])
    extends StatisticOrganizer[F] {

  override def getOrganizationMembersCount(
    id:           OrganizationID,
    byRole:       Option[OrganizationRole],
    searchFilter: Option[String],
  )(implicit ctx: UserAuthCtx): F[Count] =
    for {
      count <- organizationStatisticsAlgebra.getOrganizationMembersCount(id, byRole, searchFilter)
    } yield count

  override def getTicketsCount(id: OrganizationID, byCategory: Option[TicketCategory], searchFilter: Option[String])(
    implicit ctx:                  UserAuthCtx,
  ): F[Count] =
    for {
      count <- ticketStatisticsAlgebra.getTicketsCount(id, byCategory, searchFilter)
    } yield count

  override def getStatisticsForTickets(
    id:           OrganizationID,
    byCategory:   TicketCategory,
    byInterval:   IntervalType,
    howMany:      Option[StatisticsSize],
    until:        Option[StatisticsTimestamp],
  )(implicit ctx: UserAuthCtx): F[List[TicketStatistic]] =
    for {
      now <- F.delay(LocalDateTime.now)
      statistics <- ticketStatisticsAlgebra.getCountStats(
        organizationId = id,
        byCategory     = byCategory,
        byInterval     = byInterval,
        howMany        = howMany.getOrElse(StatisticsSize.spook(7)),
        until          = until.getOrElse(StatisticsTimestamp.spook(now)),
      )
    } yield statistics
}
