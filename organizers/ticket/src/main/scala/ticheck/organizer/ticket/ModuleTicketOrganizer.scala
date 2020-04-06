package ticheck.organizer.ticket

import ticheck.effect.Async
import ticheck.algebra.ticket.ModuleTicketAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleTicketOrganizer[F[_]] { this: ModuleTicketAlgebra[F] =>

  implicit protected def F: Async[F]

}
