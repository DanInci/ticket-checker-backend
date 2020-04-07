package ticheck.rest

import ticheck.algebra.user.models.User
import ticheck.algebra.organization.models.Organization

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class UserAuthCtx(
  user:         User,
  organization: Organization,
)
