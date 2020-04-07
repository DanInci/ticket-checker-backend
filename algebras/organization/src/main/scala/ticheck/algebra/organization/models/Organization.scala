package ticheck.algebra.organization.models

import ticheck.OrganizationID
import ticheck.dao.organization._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class Organization(
  id:   OrganizationID,
  name: OrganizationName,
)
