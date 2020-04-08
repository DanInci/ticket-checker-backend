package ticheck.algebra.user.models.auth

import ticheck.OrganizationID
import ticheck.algebra.user.models.UserProfile

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class UserAuthCtx(
  user:          UserProfile,
  organizations: List[OrganizationID],
)
