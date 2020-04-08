package ticheck.dao.organization.membership

import ticheck.{OrganizationMembershipID, UserID}
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.db.{ConnectionIO, DAOAlgebra}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationMembershipSQL[H[_]] extends DAOAlgebra[H, OrganizationMembershipRecord, OrganizationMembershipID] {

  def getByUserID(userId: UserID): ConnectionIO[List[OrganizationMembershipRecord]]

}
