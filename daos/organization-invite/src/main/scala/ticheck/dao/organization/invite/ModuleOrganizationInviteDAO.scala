package ticheck.dao.organization.invite

import ticheck.db.ConnectionIO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait ModuleOrganizationInviteDAO[F[_]] { this: ModuleTimeAlgebra[F] =>

  implicit protected def S: Sync[F]

  def organizationInviteSQL: F[OrganizationInviteSQL[ConnectionIO]] = _organizationInviteSql

  private lazy val _organizationInviteSql: F[OrganizationInviteSQL[ConnectionIO]] =
    timeAlgebra.flatMap(t => impl.OrganizationInviteSQLImpl.sync(t))

}
