package ticheck.dao.organization.models

import ticheck._
import ticheck.dao.organization._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
final case class OrganizationTable(
  id:        OrganizationID,
  name:      OrganizationName,
  createdAt: CreatedAt,
)
