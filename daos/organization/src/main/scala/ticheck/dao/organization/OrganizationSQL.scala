package ticheck.dao.organization

import ticheck.OrganizationID
import ticheck.dao.organization.models.OrganizationRecord
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait OrganizationSQL[H[_]] extends DAOAlgebra[H, OrganizationRecord, OrganizationID]
