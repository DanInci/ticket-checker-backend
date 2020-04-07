package ticheck.rest.routes

import ticheck.effect.Async
import org.http4s.dsl.Http4sDsl
import ticheck.organizer.ticket.TicketOrganizer
import ticheck.rest.UserAuthCtxRoutes

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class TicketRoutes[F[_]](ticketOrganizer: TicketOrganizer[F])(implicit val F: Async[F])
    extends Http4sDsl[F] {

  val authedRoutes: UserAuthCtxRoutes[F] = ???

}
