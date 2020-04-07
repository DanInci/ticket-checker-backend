package ticheck.organizer.organization

import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationOrganizer[F[_]] {}

object OrganizationOrganizer {

  def apply[F[_]: Sync]: F[OrganizationOrganizer[F]] = ???

}
