package ticheck.dao.ticket

import ticheck.db.ConnectionIO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleTicketDAO[F[_]] { this: ModuleTimeAlgebra[F] =>

  protected def F: Sync[F]

  def ticketSQL: F[TicketSQL[ConnectionIO]] = _ticketSql

  private lazy val _ticketSql: F[TicketSQL[ConnectionIO]] =
    timeAlgebra.flatMap(t => impl.TicketSQLImpl.sync(t))

}
