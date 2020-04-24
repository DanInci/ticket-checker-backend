package ticheck.algebra.organization

import ticheck.dao.organization.ModuleOrganizationDAO
import ticheck.dao.organization.invite.ModuleOrganizationInviteDAO
import ticheck.dao.organization.membership.ModuleOrganizationMembershipDAO
import ticheck.dao.user.ModuleUserDAO
import ticheck.db.Transactor
import ticheck.effect._
import ticheck.email.ModuleEmailAlgebra
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleOrganizationAlgebra[F[_]] {
  this: ModuleUserDAO[F]
    with ModuleOrganizationDAO[F] with ModuleOrganizationInviteDAO[F] with ModuleOrganizationMembershipDAO[F]
    with ModuleTimeAlgebra[F] with ModuleEmailAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def organizationModuleAlgebra: F[OrganizationModuleAlgebra[F]] = _organizationModuleAlgebra

  private lazy val _organizationModuleAlgebra: F[OrganizationModuleAlgebra[F]] =
    for {
      ta    <- timeAlgebra
      ea    <- emailAlgebra
      usql  <- userSQL
      osql  <- organizationSQL
      oisql <- organizationInviteSQL
      omsql <- organizationMembershipSQL
      oa    <- impl.OrganizationAlgebraImpl.async(ta, ea, usql, osql, oisql, omsql)
    } yield oa

}
