package ticheck.dao.organization

import ticheck.db.ConnectionIO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleOrganizationDAO[F[_]] { this: ModuleTimeAlgebra[F] =>

  implicit protected def S: Sync[F]

  def organizationSQL: F[OrganizationSQL[ConnectionIO]] = _organizationSql

  private lazy val _organizationSql: F[OrganizationSQL[ConnectionIO]] =
    timeAlgebra.flatMap(t => impl.OrganizationSQLImpl.sync(t))

}
