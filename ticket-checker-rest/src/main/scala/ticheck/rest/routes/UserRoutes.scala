package ticheck.rest.routes

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ticheck.effect.Async
import ticheck.organizer.user.UserOrganizer
import ticheck.rest.UserAuthCtxRoutes

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class UserRoutes[F[_]](userOrganizer: UserOrganizer[F])(implicit val F: Async[F])
    extends Http4sDsl[F] {

  val authedRoutes: UserAuthCtxRoutes[F] = ???

  val routes: HttpRoutes[F] = ???

}
