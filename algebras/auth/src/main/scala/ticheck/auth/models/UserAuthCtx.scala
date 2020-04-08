package ticheck.auth.models

import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.dao.user.models.UserRecord
import ticheck.{Email, Name, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserAuthCtx(
  userId:        UserID,
  email:         Email,
  name:          Name,
  organizations: List[OrganizationAuthCtx],
)

object UserAuthCtx {

  def from(userDao: UserRecord, membershipDaos: List[OrganizationMembershipRecord]): UserAuthCtx =
    UserAuthCtx(
      userDao.id,
      userDao.email,
      userDao.name,
      membershipDaos.map(OrganizationAuthCtx.from),
    )

}
