package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.dsl.Http4sDsl
import ticheck.effect._
import ticheck.http.{QueryParamInstances, RoutesHelpers}
import ticheck.organizer.organization.OrganizationOrganizer
import ticheck.rest._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] class OrganizationRoutes[F[_]](
  private val organizationOrganizer: OrganizationOrganizer[F],
)(implicit val F:                    Async[F])
    extends Http4sDsl[F] with RoutesHelpers with QueryParamInstances {

  object InviteCodeQueryParamMatcher extends QueryParamDecoderMatcher[String]("code")

  private val organizationRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case POST -> Root / `organizations-route` as user =>
      for {
        resp <- Created()
      } yield resp

    case GET -> Root / `organizations-route` :? PageOffsetMatcher(offset) +& PageLimitMatcher(limit) as user =>
      for {
        resp <- Ok()
      } yield resp

    case GET -> Root / `organizations-route` / FUUIDVar(orgId) as user =>
      for {
        resp <- Ok()
      } yield resp

    case PUT -> Root / `organizations-route` / FUUIDVar(orgId) as user =>
      for {
        resp <- NoContent()
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(orgId) as user =>
      for {
        resp <- NoContent()
      } yield resp
  }

  private val organizationInviteRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case POST -> Root / `organizations-route` / FUUIDVar(orgId) / "invite" as user =>
      for {
        resp <- Created()
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(orgId) / "invite" / FUUIDVar(inviteId) as user =>
      for {
        resp <- Created()
      } yield resp

    case GET -> Root / `organizations-route` / "join" :? InviteCodeQueryParamMatcher(code) as user =>
      for {
        resp <- Ok()
      } yield resp
  }

  private val organizationMembershipRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `organizations-route` / FUUIDVar(orgId) / `users-route`
          :? PageOffsetMatcher(offset) +& PageLimitMatcher(limit) as user =>
      for {
        resp <- Ok()
      } yield resp

    case PUT -> Root / `organizations-route` / FUUIDVar(orgId) / `users-route` / FUUIDVar(userId) as user =>
      for {
        resp <- Ok()
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(orgId) / `users-route` / FUUIDVar(userId) as user =>
      for {
        resp <- NoContent()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] =
    NonEmptyList.of(organizationRoutes, organizationInviteRoutes, organizationMembershipRoutes).reduceK

}
