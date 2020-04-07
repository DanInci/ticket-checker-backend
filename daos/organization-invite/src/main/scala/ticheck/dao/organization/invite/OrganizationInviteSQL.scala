package ticheck.dao.organization.invite

import ticheck.db.DAOAlgebra
import ticheck.OrganizationMembershipID
import ticheck.dao.organization.invite.models.OrganizationInviteRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationInviteSQL[H[_]] extends DAOAlgebra[H, OrganizationInviteRecord, OrganizationMembershipID]
