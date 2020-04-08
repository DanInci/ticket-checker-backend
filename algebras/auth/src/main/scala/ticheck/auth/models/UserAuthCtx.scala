package ticheck.auth.models

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
