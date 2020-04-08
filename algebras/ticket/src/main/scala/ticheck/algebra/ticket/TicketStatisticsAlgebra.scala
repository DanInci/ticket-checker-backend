package ticheck.algebra.ticket

import ticheck.OrganizationID
import ticheck.algebra.ticket.models.TicketStatistic

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
trait TicketStatisticsAlgebra[F[_]] {

  def getCountStats(
    organizationId: OrganizationID,
    byCategory:     TicketCategory,
    byInterval:     IntervalType,
    howMany:        StatisticsSize,
    until:          StatisticsTimestamp,
  ): F[List[TicketStatistic]]

}
