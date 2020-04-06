package ticheck.app

import org.http4s.HttpRoutes
import ticheck.algebra.organization.ModuleOrganizationAlgebra
import ticheck.algebra.ticket.ModuleTicketAlgebra
import ticheck.algebra.user.ModuleUserAlgebra
import ticheck.dao.organization.ModuleOrganizationDAO
import ticheck.dao.ticket.ModuleTicketDAO
import ticheck.dao.user.ModuleUserDAO
import ticheck.effect._
import ticheck.db._
import ticheck.organizer.organization.ModuleOrganizationOrganizer
import ticheck.organizer.ticket.ModuleTicketOrganizer
import ticheck.organizer.user.ModuleUserOrganizer
import ticheck.time.{ModuleTimeAlgebra, TimeConfig}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait ModuleTicketChecker[F[_]]
    extends ModuleOrganizationOrganizer[F] with ModuleOrganizationAlgebra[F] with ModuleOrganizationDAO[F]
    with ModuleTicketOrganizer[F] with ModuleTicketAlgebra[F] with ModuleTicketDAO[F] with ModuleUserOrganizer[F]
    with ModuleUserAlgebra[F] with ModuleUserDAO[F] with ModuleTimeAlgebra[F] {

  override protected def transactor: Transactor[F]

  override protected def S: Sync[F] = C

  override protected def F: Async[F] = C

  protected def C: Concurrent[F]

  protected def allConfigs: AllConfigs

  override protected def timeConfig: TimeConfig = allConfigs.timeConfig

  def routes: F[HttpRoutes[F]] = C.delay(HttpRoutes.empty[F](C))

}

object ModuleTicketChecker {

  def concurrent[F[_]](ac: AllConfigs)(
    implicit
    c:          Concurrent[F],
    tim:        Timer[F],
    t:          Transactor[F],
    cs:         ContextShift[F],
    bioShifter: BlockingShifter[F],
  ): F[ModuleTicketChecker[F]] = c.delay {
    new ModuleTicketChecker[F] {
      override protected def C: Concurrent[F] = c

      override protected def transactor: Transactor[F] = t

      override protected def allConfigs: AllConfigs = ac
    }
  }
}
