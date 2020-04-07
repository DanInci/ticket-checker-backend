package ticheck.organizer.user

import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait UserOrganizer[F[_]] {}

object UserOrganizer {

  def apply[F[_]: Sync]: F[UserOrganizer[F]] = ???

}
