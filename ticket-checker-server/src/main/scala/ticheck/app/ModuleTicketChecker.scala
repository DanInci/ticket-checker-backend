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
import ticheck.rest.{ModuleTicketCheckerRest, UserAuthedHttp4s, UserCtxMiddleware}
import ticheck.time.{ModuleTimeAlgebra, TimeConfig}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait ModuleTicketChecker[F[_]]
    extends ModuleTicketCheckerRest[F] with ModuleOrganizationAlgebra[F] with ModuleOrganizationDAO[F]
    with ModuleTicketAlgebra[F] with ModuleTicketDAO[F] with ModuleUserAlgebra[F] with ModuleUserDAO[F]
    with ModuleTimeAlgebra[F] {

  override protected def transactor: Transactor[F]

  override protected def S: Sync[F] = C

  override protected def F: Async[F] = C

  implicit protected def C: Concurrent[F]

  protected def allConfigs: AllConfigs

  override protected def timeConfig: TimeConfig = allConfigs.timeConfig

  def serverRoutes: F[HttpRoutes[F]] = _serverRoutes

  private lazy val authCtxMiddleware: F[UserCtxMiddleware[F]] =
    for {
      oa             <- organizationAlgebra
      ua             <- userAlgebra
      authMiddleware <- UserAuthedHttp4s.sync[F](allConfigs.jwtAuthConfig, ua, oa)(S)
    } yield authMiddleware

  private lazy val _serverRoutes: F[HttpRoutes[F]] =
    for {
      middleware <- authCtxMiddleware
      routes     <- routes
      authed     <- authedRoutes
      toCombine = middleware(authed)
    } yield routes <+> toCombine

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
