package ticheck.dao.ticket

import ticheck.{Limit, Offset, OrganizationID, UserID}
import ticheck.db.DAOAlgebra
import ticheck.dao.ticket.models.TicketRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait TicketSQL[H[_]] extends DAOAlgebra[H, TicketRecord, TicketPK] {

  def getAllForOrganization(
    organizationId: OrganizationID,
    offset:         Offset,
    limit:          Limit,
    byCategory:     Option[TicketCategory],
    byUserId:       Option[UserID],
    searchVal:      Option[String],
  ): H[List[TicketRecord]]

  def findByUserID(userId: UserID, category: Option[TicketCategory] = None): H[List[TicketRecord]]

  def countTicketsBetweenDates(
    byCategory: TicketCategory,
    startDate:  StartDate,
    endDate:    EndDate,
  ): H[Count]

}
