package ticheck.organizer.statistic.impl

import java.time.LocalDateTime

import ticheck.OrganizationID
import ticheck.algebra.ticket.models.TicketStatistic
import ticheck.algebra.ticket._
import ticheck.auth.models.UserAuthCtx
import ticheck.organizer.statistic.StatisticOrganizer
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[statistic] case class StatisticOrganizerImpl[F[_]](
  private val ticketStatisticsAlgebra: TicketStatisticsAlgebra[F],
)(implicit F:                          Sync[F])
    extends StatisticOrganizer[F] {

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
