package ticheck.auth.models

import ticheck.OrganizationID
import ticheck.dao.organization.membership.OrganizationRole
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationAuthCtx(
  id:   OrganizationID,
  role: OrganizationRole,
)

object OrganizationAuthCtx {

  def from(membershipDAO: OrganizationMembershipRecord): OrganizationAuthCtx =
    OrganizationAuthCtx(
      membershipDAO.organizationId,
      membershipDAO.role,
    )

}
