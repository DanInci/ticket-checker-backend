package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ticheck.UserID
import ticheck.algebra.user.models.{UserDefinition, UserLoginRequest, UserRegistration}
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
  }

  private val registerLoginRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "register" =>
      for {
        regData <- req.as[UserRegistration]
        _       <- userOrganizer.register(regData)
        resp    <- Created()
      } yield resp

    case req @ POST -> Root / "login" =>
      for {
        loginData     <- req.as[UserLoginRequest]
        loginResponse <- userOrganizer.login(loginData)
        resp          <- Ok(loginResponse)
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] = usersRoutes

  val routes: HttpRoutes[F] = registerLoginRoutes

}
