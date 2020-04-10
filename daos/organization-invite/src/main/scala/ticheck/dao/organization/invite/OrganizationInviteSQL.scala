package ticheck.dao.organization.invite

import ticheck.db.DAOAlgebra
import ticheck.{Email, Limit, Offset, OrganizationID, OrganizationInviteID, UserID}
import ticheck.dao.organization.invite.models.OrganizationInviteRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationInviteSQL[H[_]] extends DAOAlgebra[H, OrganizationInviteRecord, OrganizationInviteID] {

  def getAllForUser(
    userId:       UserID,
    offset:       Offset,
    limit:        Limit,
    statusFilter: Option[InviteStatus],
  ): H[List[OrganizationInviteRecord]]

  def findForOrganizationByEmail(id: OrganizationID, email: Email): H[Option[OrganizationInviteRecord]]

  def findByInvitationCode(code: InviteCode): H[Option[OrganizationInviteRecord]]

}
