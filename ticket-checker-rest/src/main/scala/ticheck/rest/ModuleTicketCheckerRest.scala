package ticheck.rest

import org.http4s.HttpRoutes
import ticheck.algebra.organization.ModuleOrganizationAlgebra
import ticheck.algebra.ticket.ModuleTicketAlgebra
import ticheck.algebra.user.ModuleUserAlgebra
import ticheck.effect._
import ticheck.organizer.organization.OrganizationOrganizer
import ticheck.organizer.statistic.StatisticOrganizer
import ticheck.organizer.ticket.TicketOrganizer
import ticheck.organizer.user.UserOrganizer
import ticheck.rest.routes.{OrganizationRoutes, StatisticRoutes, TicketRoutes, UserRoutes}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait ModuleTicketCheckerRest[F[_]] {
  this: ModuleOrganizationAlgebra[F] with ModuleUserAlgebra[F] with ModuleTicketAlgebra[F] =>

  implicit protected def F: Async[F]

  protected def routes: F[HttpRoutes[F]] = _routes

  protected def authedRoutes: F[UserAuthCtxRoutes[F]] = _authedRoutes

  protected def organizationRoutes: F[OrganizationRoutes[F]] = _organizationRoutes

  protected def userRoutes: F[UserRoutes[F]] = _userRoutes

  protected def ticketRoutes: F[TicketRoutes[F]] = _ticketRoutes

  protected def statisticRoutes: F[StatisticRoutes[F]] = _statisticRoutes

  private lazy val _organizationRoutes: F[OrganizationRoutes[F]] =
    for {
      oa           <- organizationAlgebra
      orgOrganizer <- OrganizationOrganizer[F](oa)
    } yield new OrganizationRoutes[F](orgOrganizer)

  private lazy val _userRoutes: F[UserRoutes[F]] =
    for {
      ua            <- userAlgebra
      userOrganizer <- UserOrganizer[F](ua)
    } yield new UserRoutes[F](userOrganizer)

  private lazy val _ticketRoutes: F[TicketRoutes[F]] =
    for {
      ta              <- ticketAlgebra
      ticketOrganizer <- TicketOrganizer[F](ta)
    } yield new TicketRoutes[F](ticketOrganizer)

  private lazy val _statisticRoutes: F[StatisticRoutes[F]] =
    for {
      tsa                <- ticketStatisticsAlgebra
      statisticOrganizer <- StatisticOrganizer[F](tsa)
    } yield new StatisticRoutes[F](statisticOrganizer)

  private lazy val _routes: F[HttpRoutes[F]] =
    userRoutes.map(_.routes)

  private lazy val _authedRoutes: F[UserAuthCtxRoutes[F]] =
    for {
      org       <- organizationRoutes
      user      <- userRoutes
      ticket    <- ticketRoutes
      statistic <- statisticRoutes
    } yield NonEmptyList.of(org.authedRoutes, user.authedRoutes, ticket.authedRoutes, statistic.authedRoutes).reduceK

}
