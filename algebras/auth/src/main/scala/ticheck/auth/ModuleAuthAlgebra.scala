package ticheck.auth

import ticheck.dao.organization.membership.ModuleOrganizationMembershipDAO
import ticheck.dao.user.ModuleUserDAO
import ticheck.effect._
import ticheck.db.Transactor
import ticheck.email.ModuleEmailAlgebra
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
trait ModuleAuthAlgebra[F[_]] {
  this: ModuleUserDAO[F] with ModuleOrganizationMembershipDAO[F] with ModuleTimeAlgebra[F] with ModuleEmailAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  protected def authConfig: JWTAuthConfig

  def authAlgebra: F[AuthAlgebra[F]] = _authAlgebra

  private lazy val _authAlgebra: F[AuthAlgebra[F]] =
    for {
      ta    <- timeAlgebra
      ea    <- emailAlgebra
      usql  <- userSQL
      omsql <- organizationMembershipSQL
      au    <- impl.AuthAlgebraImpl.async(authConfig, ta, ea, usql, omsql)
    } yield au

}
