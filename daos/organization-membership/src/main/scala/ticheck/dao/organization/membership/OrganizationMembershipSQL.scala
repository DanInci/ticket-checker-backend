package ticheck.dao.organization.membership

import ticheck.{Email, Limit, Offset, OrganizationID, OrganizationMembershipID, UserID}
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.db.DAOAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationMembershipSQL[H[_]] extends DAOAlgebra[H, OrganizationMembershipRecord, OrganizationMembershipID] {

  def getAllForOrganization(
    organizationId: OrganizationID,
    offset:         Offset,
    limit:          Limit,
  ): H[List[OrganizationMembershipRecord]]

  def findForOrganizationByUserID(
    organizationId: OrganizationID,
    userId:         UserID,
  ): H[Option[OrganizationMembershipRecord]]

  def findForOrganizationByEmail(organizationId: OrganizationID, email: Email): H[Option[OrganizationMembershipRecord]]

  def getByUserID(userId: UserID): H[List[OrganizationMembershipRecord]]

}
