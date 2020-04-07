package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ticheck.effect._
import ticheck.http.RoutesHelpers
import ticheck.rest._
import ticheck.organizer.user.UserOrganizer
import ticheck.rest.UserAuthCtxRoutes

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class UserRoutes[F[_]](private val userOrganizer: UserOrganizer[F])(implicit val F: Async[F])
    extends Http4sDsl[F] with RoutesHelpers {

  private val usersRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `users-route` / FUUIDVar(uid) as user =>
      for {
        resp <- Ok()
      } yield resp

    case req @ PUT -> Root / `users-route` / FUUIDVar(uid) as user =>
      for {
        resp <- Ok()
      } yield resp

    case DELETE -> Root / `users-route` / FUUIDVar(uid) as user =>
      for {
        resp <- NoContent()
      } yield resp
  }

  private val registerLoginRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "register" =>
      for {
        resp <- Created()
      } yield resp

    case req @ POST -> Root / "login" =>
      for {
        resp <- Ok()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] = usersRoutes

  val routes: HttpRoutes[F] = registerLoginRoutes

}
