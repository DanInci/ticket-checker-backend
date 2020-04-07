package ticheck.rest

import org.http4s.HttpRoutes
import ticheck.effect._
import ticheck.organizer.organization.ModuleOrganizationOrganizer
import ticheck.organizer.ticket.ModuleTicketOrganizer
import ticheck.organizer.user.ModuleUserOrganizer
import ticheck.rest.routes.{OrganizationRoutes, TicketRoutes, UserRoutes}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait ModuleTicketCheckerRest[F[_]] {
  this: ModuleOrganizationOrganizer[F] with ModuleUserOrganizer[F] with ModuleTicketOrganizer[F] =>

  implicit protected def F: Async[F]

  protected def routes: F[HttpRoutes[F]] = _routes

  protected def authedRoutes: F[UserAuthCtxRoutes[F]] = _authedRoutes

  protected def organizationRoutes: F[OrganizationRoutes[F]] = _organizationRoutes

  protected def userRoutes: F[UserRoutes[F]] = _userRoutes

  protected def ticketRoutes: F[TicketRoutes[F]] = _ticketRoutes

  private lazy val _organizationRoutes: F[OrganizationRoutes[F]] =
    for {
      orgOrganizer <- organizationOrganizer
    } yield new OrganizationRoutes[F](orgOrganizer)

  private lazy val _userRoutes: F[UserRoutes[F]] =
    for {
      userOrganizer <- userOrganizer
    } yield new UserRoutes[F](userOrganizer)

  private lazy val _ticketRoutes: F[TicketRoutes[F]] =
    for {
      ticketOrganizer <- ticketOrganizer
    } yield new TicketRoutes[F](ticketOrganizer)

  private lazy val _routes: F[HttpRoutes[F]] =
    for {
      org  <- organizationRoutes
      user <- userRoutes
    } yield NonEmptyList.of(org.routes, user.routes).reduceK

  private lazy val _authedRoutes: F[UserAuthCtxRoutes[F]] =
    for {
      org    <- organizationRoutes
      user   <- userRoutes
      ticket <- ticketRoutes
    } yield NonEmptyList.of(org.authedRoutes, user.authedRoutes, ticket.authedRoutes).reduceK

}
