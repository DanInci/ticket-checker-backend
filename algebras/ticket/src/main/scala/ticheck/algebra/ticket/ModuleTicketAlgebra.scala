package ticheck.algebra.ticket

import ticheck.dao.organization.ModuleOrganizationDAO
import ticheck.dao.ticket.ModuleTicketDAO
import ticheck.dao.user.ModuleUserDAO
import ticheck.db.Transactor
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleTicketAlgebra[F[_]] {
  this: ModuleUserDAO[F] with ModuleTicketDAO[F] with ModuleOrganizationDAO[F] with ModuleTimeAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def ticketModuleAlgebra: F[TicketModuleAlgebra[F]] = _ticketModuleAlgebra

  private lazy val _ticketModuleAlgebra: F[TicketModuleAlgebra[F]] =
    for {
      ta   <- timeAlgebra
      usql <- userSQL
      tsql <- ticketSQL
      osql <- organizationSQL
      ta   <- impl.TicketAlgebraImpl.async(ta, usql, tsql, osql)
    } yield ta

}
