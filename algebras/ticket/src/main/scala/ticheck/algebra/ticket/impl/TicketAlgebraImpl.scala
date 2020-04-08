package ticheck.algebra.ticket.impl

import ticheck.algebra.ticket.{TicketAlgebra, TicketModuleAlgebra, TicketStatisticsAlgebra}
import ticheck.dao.ticket.TicketSQL
import ticheck.effect._
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[ticket] class TicketAlgebraImpl[F[_]] private (
  ticketSQL:  TicketSQL[ConnectionIO],
)(implicit F: Async[F], transactor: Transactor[F])
    extends TicketAlgebra[F] with TicketStatisticsAlgebra[F] with DBOperationsAlgebra[F] {}

private[ticket] object TicketAlgebraImpl {

  def async[F[_]: Async: Transactor](ticketSQL: TicketSQL[ConnectionIO]): F[TicketModuleAlgebra[F]] =
    Async[F].pure(new TicketAlgebraImpl[F](ticketSQL))

}
