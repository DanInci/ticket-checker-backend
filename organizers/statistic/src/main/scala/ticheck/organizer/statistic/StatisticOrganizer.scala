package ticheck.organizer.statistic

import ticheck.algebra.organization.OrganizationStatisticsAlgebra
import ticheck.organizer.statistic.impl.StatisticOrganizerImpl
import ticheck.{Count, OrganizationID}
import ticheck.algebra.ticket._
import ticheck.algebra.ticket.models.TicketStatistic
import ticheck.auth.models.UserAuthCtx
import ticheck.dao.organization.membership.OrganizationRole
import ticheck.dao.ticket.TicketCategory
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait StatisticOrganizer[F[_]] {

  def getOrganizationMembersCount(
    id:           OrganizationID,
    byRole:       Option[OrganizationRole] = None,
    searchFilter: Option[String] = None,
  )(implicit ctx: UserAuthCtx): F[Count]

  def getTicketsCount(
    id:           OrganizationID,
    byCategory:   Option[TicketCategory] = None,
    searchFilter: Option[String] = None,
  )(implicit ctx: UserAuthCtx): F[Count]

  def getStatisticsForTickets(
    id:           OrganizationID,
    byCategory:   TicketCategory,
    byInterval:   IntervalType,
    howMany:      Option[StatisticsSize] = None,
    until:        Option[StatisticsTimestamp] = None,
  )(implicit ctx: UserAuthCtx): F[List[TicketStatistic]]

}

object StatisticOrganizer {

  def apply[F[_]: Sync](
    organizationStatisticsAlgebra: OrganizationStatisticsAlgebra[F],
    ticketStatisticsAlgebra:       TicketStatisticsAlgebra[F],
  ): F[StatisticOrganizer[F]] =
    Sync[F].pure(new StatisticOrganizerImpl[F](organizationStatisticsAlgebra, ticketStatisticsAlgebra))

}
