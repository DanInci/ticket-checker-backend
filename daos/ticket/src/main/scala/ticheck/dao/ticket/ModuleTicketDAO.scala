package ticheck.dao.ticket

import ticheck.db.ConnectionIO
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleTicketDAO[F[_]] {

  protected def F: Sync[F]

  def ticketSQL: F[TicketSQL[ConnectionIO]] = _ticketSql

  private lazy val _ticketSql: F[TicketSQL[ConnectionIO]] = F.pure(impl.TicketSQLImpl)

}
