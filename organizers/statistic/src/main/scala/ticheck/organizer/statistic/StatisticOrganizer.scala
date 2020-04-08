package ticheck.organizer.statistic

import ticheck.OrganizationID
import ticheck.algebra.ticket._
import ticheck.algebra.ticket.models.TicketStatistic
import ticheck.algebra.user.models.auth.UserAuthCtx
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait StatisticOrganizer[F[_]] {

  def getStatisticsForTickets(
    id:           OrganizationID,
    byCategory:   TicketCategory,
    byInterval:   IntervalType,
    howMany:      Option[StatisticsSize] = None,
    until:        Option[StatisticsTimestamp] = None,
  )(implicit ctx: UserAuthCtx): F[List[TicketStatistic]]

}

object StatisticOrganizer {

  def apply[F[_]: Sync](ticketStatisticsAlgebra: TicketStatisticsAlgebra[F]): F[StatisticOrganizer[F]] =
    Sync[F].pure(new impl.StatisticOrganizerImpl[F](ticketStatisticsAlgebra))

}
