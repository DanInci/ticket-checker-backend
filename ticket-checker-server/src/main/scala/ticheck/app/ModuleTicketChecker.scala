package ticheck.app

import org.http4s.HttpRoutes
import ticheck.effect._
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait ModuleTicketChecker[F[_]] {

  implicit protected def C: Concurrent[F]

  def routes: F[HttpRoutes[F]] = C.delay(HttpRoutes.empty[F])

}

object ModuleTicketChecker {

  def concurrent[F[_]](allConfigs: AllConfigs)(
    implicit
    c:          Concurrent[F],
    tim:        Timer[F],
    t:          Transactor[F],
    cs:         ContextShift[F],
    bioShifter: BlockingShifter[F],
  ): F[ModuleTicketChecker[F]] = c.delay {
    new ModuleTicketChecker[F] {
      override protected def C: Concurrent[F] = c
    }
  }
}
