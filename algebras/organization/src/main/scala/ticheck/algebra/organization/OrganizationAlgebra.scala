package ticheck.algebra.organization

import ticheck.OrganizationID
import ticheck.algebra.organization.models.Organization

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationAlgebra[F[_]] {

  def getById(id: OrganizationID): F[Organization]

}
