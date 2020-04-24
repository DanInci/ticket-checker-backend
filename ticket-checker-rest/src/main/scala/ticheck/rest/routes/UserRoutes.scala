package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.{HttpRoutes, ParseFailure, QueryParamDecoder, QueryParameterValue}
import org.http4s.dsl.Http4sDsl
import ticheck.{PagingInfo, UserID}
import ticheck.algebra.user.models.UserDefinition
import ticheck.auth.models.{LoginRequest, RegistrationRequest}
import ticheck.dao.organization.invite.InviteStatus
import ticheck.dao.user.VerificationCode
import ticheck.effect._
import ticheck.http.{QueryParamInstances, RoutesHelpers}
import ticheck.rest._
import ticheck.http._
import ticheck.organizer.user.UserOrganizer
import ticheck.rest.UserAuthCtxRoutes

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class UserRoutes[F[_]](private val userOrganizer: UserOrganizer[F])(implicit val F: Async[F])
    extends Http4sDsl[F] with RoutesHelpers with QueryParamInstances {

  implicit val inviteStatusQueryParamDecoder: QueryParamDecoder[InviteStatus] =
    (value: QueryParameterValue) =>
      InviteStatus
        .fromString(value.value)
        .leftMap(t => ParseFailure("Query param decoding failed", t.getMessage))
        .toValidatedNel

  implicit def verificationCodeQueryParamMatcher: QueryParamDecoder[VerificationCode] =
    phantomTypeQueryParamDecoder[F, String, VerificationCode.Tag]

  object InviteStatusQueryParamMatcher     extends OptionalQueryParamDecoderMatcher[InviteStatus]("status")
  object VerificationCodeQueryParamMatcher extends QueryParamDecoderMatcher[VerificationCode]("code")

  private val usersRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `users-route` / FUUIDVar(uid) as user =>
      for {
        profile <- userOrganizer.getUserProfile(UserID.spook(uid))(user)
        resp    <- Ok(profile)
      } yield resp

    case (req @ PUT -> Root / `users-route` / FUUIDVar(uid)) as user =>
      for {
        newProfile     <- req.as[UserDefinition]
        updatedProfile <- userOrganizer.updateUserProfile(UserID.spook(uid), newProfile)(user)
        resp           <- Ok(updatedProfile)
      } yield resp

    case DELETE -> Root / `users-route` / FUUIDVar(uid) as user =>
      for {
        _    <- userOrganizer.deleteUser(UserID.spook(uid))(user)
        resp <- NoContent()
      } yield resp

    case GET -> Root / `users-route` / FUUIDVar(uid) / `invites-route` :? PageNumberMatcher(pageNumber)
          +& PageSizeMatcher(pageSize) +& InviteStatusQueryParamMatcher(inviteStatus) as user =>
      for {
        invites <- userOrganizer.getUserInvites(UserID.spook(uid), PagingInfo(pageNumber, pageSize), inviteStatus)(user)
        resp    <- Ok(invites)
      } yield resp
  }

  private val registerLoginRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "register" =>
      for {
        regData <- req.as[RegistrationRequest]
        _       <- userOrganizer.register(regData)
        resp    <- Created()
      } yield resp

    case POST -> Root / "verify" :? VerificationCodeQueryParamMatcher(code) =>
      for {
        _    <- userOrganizer.verifyAccount(code)
        resp <- Ok()
      } yield resp

    case req @ POST -> Root / "login" =>
      for {
        loginData     <- req.as[LoginRequest]
        loginResponse <- userOrganizer.login(loginData)
        resp          <- Ok(loginResponse)
      } yield resp

    case GET -> Root / "health" => Ok()
  }

  val authedRoutes: UserAuthCtxRoutes[F] = usersRoutes

  val routes: HttpRoutes[F] = registerLoginRoutes

}
