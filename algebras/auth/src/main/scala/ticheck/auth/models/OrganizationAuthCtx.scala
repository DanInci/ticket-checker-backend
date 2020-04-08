package ticheck.auth.models

import ticheck.OrganizationID
import ticheck.dao.organization.membership.OrganizationRole

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
