package ticheck.auth.models

import ticheck.{Email, Name, OrganizationID, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserAuthCtx(
  userId:          UserID,
  email:           Email,
  name:            Name,
  organizationIds: List[OrganizationID],
)
