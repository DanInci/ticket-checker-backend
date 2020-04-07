package ticheck.dao.organization.membership

import ticheck.db.ConnectionIO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait ModuleOrganizationMembershipDAO[F[_]] { this: ModuleTimeAlgebra[F] =>

  implicit protected def S: Sync[F]

  def organizationMembershipSQL: F[OrganizationMembershipSQL[ConnectionIO]] = _organizationMembershipSql

  private lazy val _organizationMembershipSql: F[OrganizationMembershipSQL[ConnectionIO]] =
    timeAlgebra.flatMap(t => impl.OrganizationMembershipSQLImpl.sync(t))

}
