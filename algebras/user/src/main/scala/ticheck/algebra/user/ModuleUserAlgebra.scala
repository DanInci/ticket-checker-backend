package ticheck.algebra.user

import ticheck.dao.organization.membership.ModuleOrganizationMembershipDAO
import ticheck.dao.ticket.ModuleTicketDAO
import ticheck.db.Transactor
import ticheck.dao.user.ModuleUserDAO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleUserAlgebra[F[_]] {
  this: ModuleUserDAO[F] with ModuleTicketDAO[F] with ModuleOrganizationMembershipDAO[F] with ModuleTimeAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def userModuleAlgebra: F[UserModuleAlgebra[F]] = _userModuleAlgebra

  private lazy val _userModuleAlgebra: F[UserModuleAlgebra[F]] =
    for {
      ta    <- timeAlgebra
      usql  <- userSQL
      tsql  <- ticketSQL
      omsql <- organizationMembershipSQL
      ua    <- impl.UserAlgebraImpl.async(ta, usql, tsql, omsql)
    } yield ua

}
