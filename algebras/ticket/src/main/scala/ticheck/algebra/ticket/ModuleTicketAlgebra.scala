package ticheck.algebra.ticket

import ticheck.dao.ticket.ModuleTicketDAO
import ticheck.db.Transactor
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleTicketAlgebra[F[_]] { this: ModuleTicketDAO[F] with ModuleTimeAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def ticketModuleAlgebra: F[TicketModuleAlgebra[F]] = _ticketModuleAlgebra

  private lazy val _ticketModuleAlgebra: F[TicketModuleAlgebra[F]] =
    for {
      tsql <- ticketSQL
      ta   <- impl.TicketAlgebraImpl.async(tsql)
    } yield ta

}
