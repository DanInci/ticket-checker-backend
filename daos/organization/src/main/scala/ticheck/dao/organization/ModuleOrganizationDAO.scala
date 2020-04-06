package ticheck.dao.organization

import ticheck.db.ConnectionIO
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleOrganizationDAO[F[_]] {

  protected def F: Sync[F]

  def organizationSQL: F[OrganizationSQL[ConnectionIO]] = _organizationSql

  private lazy val _organizationSql: F[OrganizationSQL[ConnectionIO]] = F.pure(impl.OrganizationSQLImpl)

}
