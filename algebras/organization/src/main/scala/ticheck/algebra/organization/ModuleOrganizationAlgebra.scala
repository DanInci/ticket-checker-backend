package ticheck.algebra.organization

import ticheck.dao.organization.ModuleOrganizationDAO
import ticheck.db.Transactor
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleOrganizationAlgebra[F[_]] { this: ModuleOrganizationDAO[F] with ModuleTimeAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def organizationAlgebra: F[OrganizationAlgebra[F]] = ???

}
