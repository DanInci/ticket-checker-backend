package ticheck.time

import ticheck.effect._
import java.time.OffsetDateTime

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait TimeAlgebra {

  def now[F[_]: Sync]: F[OffsetDateTime]

}
