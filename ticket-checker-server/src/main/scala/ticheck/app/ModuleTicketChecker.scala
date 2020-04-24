package ticheck.app

import org.http4s.HttpRoutes
import ticheck.algebra.organization.ModuleOrganizationAlgebra
import ticheck.algebra.ticket.ModuleTicketAlgebra
import ticheck.algebra.user.ModuleUserAlgebra
import ticheck.app.static.StaticFiles
import ticheck.auth.{JWTAuthConfig, ModuleAuthAlgebra}
import ticheck.dao.organization.ModuleOrganizationDAO
import ticheck.dao.organization.invite.ModuleOrganizationInviteDAO
import ticheck.dao.organization.membership.ModuleOrganizationMembershipDAO
import ticheck.dao.ticket.ModuleTicketDAO
import ticheck.dao.user.ModuleUserDAO
import ticheck.effect._
import ticheck.db._
import ticheck.email.{EmailConfig, ModuleEmailAlgebra}
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
    with ModuleOrganizationMembershipDAO[F] with ModuleOrganizationInviteDAO[F] with ModuleTicketAlgebra[F]
    with ModuleTicketDAO[F] with ModuleAuthAlgebra[F] with ModuleUserAlgebra[F] with ModuleUserDAO[F]
    with ModuleTimeAlgebra[F] with ModuleEmailAlgebra[F] {

  override protected def transactor: Transactor[F]

  override protected def F: Async[F] = C

  override protected def S: Sync[F] = C

  implicit protected def C: Concurrent[F]

  implicit protected def contextShift: ContextShift[F]

  implicit protected def blockingShifter: BlockingShifter[F]

  implicit protected def timer: Timer[F]

  protected def allConfigs: AllConfigs

  override protected def timeConfig: TimeConfig = allConfigs.timeConfig

  override protected def authConfig: JWTAuthConfig = allConfigs.jwtAuthConfig

  override protected def emailConfig: EmailConfig = allConfigs.emailConfig

  def serverRoutes: F[HttpRoutes[F]] = _serverRoutes

  private lazy val authCtxMiddleware: F[UserCtxMiddleware[F]] =
    for {
      aa             <- authAlgebra
      authMiddleware <- UserAuthedHttp4s.sync[F](allConfigs.jwtAuthConfig, aa)(S)
    } yield authMiddleware

  private lazy val _serverRoutes: F[HttpRoutes[F]] =
    for {
      middleware   <- authCtxMiddleware
      routes       <- routes
      staticRoutes <- StaticFiles.routes(C, contextShift, blockingShifter)
      authed       <- authedRoutes
      authedRoutes = middleware(authed)
    } yield routes <+> staticRoutes <+> authedRoutes

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
      override protected def contextShift: ContextShift[F] = cs

      override protected def blockingShifter: BlockingShifter[F] = bioShifter

      implicit protected def timer: Timer[F] = tim

      override protected def C: Concurrent[F] = c

      override protected def transactor: Transactor[F] = t

      override protected def allConfigs: AllConfigs = ac
    }
  }
}
