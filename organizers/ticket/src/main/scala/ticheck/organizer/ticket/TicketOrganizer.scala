package ticheck.organizer.ticket

import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait TicketOrganizer[F[_]] {}

object TicketOrganizer {

  def apply[F[_]: Sync]: F[TicketOrganizer[F]] = ???

}
