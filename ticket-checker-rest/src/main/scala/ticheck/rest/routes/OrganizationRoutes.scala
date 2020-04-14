package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.QueryParamDecoder
import org.http4s.dsl.Http4sDsl
import ticheck.{OrganizationID, OrganizationInviteID, PagingInfo, UserID}
import ticheck.algebra.organization.models._
import ticheck.dao.organization.invite.{InviteCode, InviteStatus}
import ticheck.effect._
import ticheck.http.{QueryParamInstances, RoutesHelpers}
import ticheck.organizer.organization.OrganizationOrganizer
import ticheck.rest._
import ticheck.http._

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

  implicit val inviteCodeQueryParamMatcher: QueryParamDecoder[InviteCode] =
    phantomTypeQueryParamDecoder[F, String, InviteCode.Tag]

  object InviteCodeQueryParamMatcher extends QueryParamDecoderMatcher[InviteCode]("code")

  private val organizationRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case (req @ POST -> Root / `organizations-route`) as user =>
      for {
        definition <- req.as[OrganizationDefinition]
        profile    <- organizationOrganizer.registerOrganization(definition)(user)
        resp       <- Created(profile)
      } yield resp

    case GET -> Root / `organizations-route` :? PageNumberMatcher(pageNumber) +& PageSizeMatcher(pageSize) as user =>
      for {
        organizations <- organizationOrganizer.getOrganizationList(PagingInfo(pageNumber, pageSize))(user)
        resp          <- Ok(organizations)
      } yield resp

    case GET -> Root / `organizations-route` / FUUIDVar(orgId) as user =>
      for {
        profile <- organizationOrganizer.getOrganizationProfile(OrganizationID.spook(orgId))(user)
        resp    <- Ok(profile)
      } yield resp

    case (req @ PUT -> Root / `organizations-route` / FUUIDVar(orgId)) as user =>
      for {
        definition <- req.as[OrganizationDefinition]
        profile    <- organizationOrganizer.updateOrganizationProfile(OrganizationID.spook(orgId), definition)(user)
        resp       <- Ok(profile)
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(orgId) as user =>
      for {
        _    <- organizationOrganizer.deleteOrganization(OrganizationID.spook(orgId))(user)
        resp <- NoContent()
      } yield resp
  }

  private val organizationInviteRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case (req @ POST -> Root / `organizations-route` / FUUIDVar(orgId) / "invite") as user =>
      for {
        definition <- req.as[OrganizationInviteDefinition]
        invite     <- organizationOrganizer.invite(OrganizationID.spook(orgId), definition)(user)
        resp       <- Created(invite)
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(orgId) / "invite" / FUUIDVar(inviteId) as user =>
      for {
        _    <- organizationOrganizer.cancelInvite(OrganizationID.spook(orgId), OrganizationInviteID.spook(inviteId))(user)
        resp <- NoContent()
      } yield resp

    case GET -> Root / `organizations-route` / "join" :? InviteCodeQueryParamMatcher(code) as user =>
      for {
        _    <- organizationOrganizer.join(code)(user)
        resp <- Ok()
      } yield resp

    case POST -> Root / `organizations-route` / FUUIDVar(orgId) / "invite" / FUUIDVar(inviteId) / "accept" as user =>
      for {
        _ <- organizationOrganizer.setInviteStatus(
          OrganizationID.spook(orgId),
          OrganizationInviteID.spook(inviteId),
          InviteStatus.InviteAccepted,
        )(user)
        resp <- Ok()
      } yield resp

    case POST -> Root / `organizations-route` / FUUIDVar(orgId) / "invite" / FUUIDVar(inviteId) / "decline" as user =>
      for {
        _ <- organizationOrganizer.setInviteStatus(
          OrganizationID.spook(orgId),
          OrganizationInviteID.spook(inviteId),
          InviteStatus.InviteDeclined,
        )(user)
        resp <- Ok()
      } yield resp
  }

  private val organizationMembershipRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `organizations-route` / FUUIDVar(orgId) / `users-route`
          :? PageNumberMatcher(pageNumber) +& PageSizeMatcher(pageSize) as user =>
      for {
        members <- organizationOrganizer
          .getOrganizationMemberList(OrganizationID.spook(orgId), PagingInfo(pageNumber, pageSize))(user)
        resp <- Ok(members)
      } yield resp

    case (req @ PUT -> Root / `organizations-route` / FUUIDVar(orgId) / `users-route` / FUUIDVar(userId)) as user =>
      for {
        definition <- req.as[OrganizationMemberDefinition]
        member <- organizationOrganizer
          .updateOrganizationMember(OrganizationID.spook(orgId), UserID.spook(userId), definition)(user)
        resp <- Ok(member)
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(orgId) / `users-route` / FUUIDVar(userId) as user =>
      for {
        _    <- organizationOrganizer.removeOrganizationMember(OrganizationID.spook(orgId), UserID.spook(userId))(user)
        resp <- NoContent()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] =
    NonEmptyList.of(organizationRoutes, organizationInviteRoutes, organizationMembershipRoutes).reduceK

}
