package ticheck.dao.organization.membership

import ticheck.OrganizationMembershipID
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.db.DAOAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationMembershipSQL[H[_]] extends DAOAlgebra[H, OrganizationMembershipRecord, OrganizationMembershipID]
