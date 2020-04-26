package ticheck.dao.ticket

import ticheck.dao.ticket.models.TicketRecord
import ticheck.db.DAOAlgebra
import ticheck.{Count, Limit, Offset, OrganizationID, UserID}

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

  def findByUserID(
    userId:   UserID,
    category: Option[TicketCategory] = None,
  ): H[List[TicketRecord]]

  def countTicketsBetweenDates(
    organizationId: OrganizationID,
    byCategory:     TicketCategory,
    startDate:      StartDate,
    endDate:        EndDate,
  ): H[Count]

  def countBy(organizationId: OrganizationID, byCategory: Option[TicketCategory], searchVal: Option[String]): H[Count]

}
