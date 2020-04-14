package ticheck

import org.http4s.AuthedRoutes
import org.http4s.server.AuthMiddleware
import ticheck.auth.models.UserAuthCtx

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
package object rest {

  type UserCtxMiddleware[F[_]] = AuthMiddleware[F, UserAuthCtx]
  type UserAuthCtxRoutes[F[_]] = AuthedRoutes[UserAuthCtx, F]

  lazy val `organizations-route` = "organizations"
  lazy val `tickets-route`       = "tickets"
  lazy val `users-route`         = "users"
  lazy val `invites-route`       = "invites"
  lazy val `statistics-route`    = "statistics"

}
