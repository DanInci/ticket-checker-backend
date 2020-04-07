package ticheck.organizer.statistic

import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait StatisticOrganizer[F[_]] {}

object StatisticOrganizer {

  def apply[F[_]: Sync]: F[StatisticOrganizer[F]] = ???

}
