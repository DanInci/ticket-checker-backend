package ticheck.algebra.ticket

import ticheck.{Count, OrganizationID}
import ticheck.algebra.ticket.models.TicketStatistic
import ticheck.dao.ticket.TicketCategory

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
trait TicketStatisticsAlgebra[F[_]] {

  def getTicketsCount(
    organizationId: OrganizationID,
    byCategory:     Option[TicketCategory],
    searchValue:    Option[String],
  ): F[Count]

  def getCountStats(
    organizationId: OrganizationID,
    byCategory:     TicketCategory,
    byInterval:     IntervalType,
    howMany:        StatisticsSize,
    until:          StatisticsTimestamp,
  ): F[List[TicketStatistic]]

}
