package ticheck.rest

import cats.Defer
import org.http4s.{AuthedRequest, AuthedRoutes, Response}
import ticheck.algebra.user.models.auth.UserAuthCtx
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
object UserAuthCtxRoutes {

  def apply[F[_]](
    pf:         PartialFunction[AuthedRequest[F, UserAuthCtx], F[Response[F]]],
  )(implicit F: Applicative[F], d: Defer[F]): AuthedRoutes[UserAuthCtx, F] =
    AuthedRoutes.of(pf)

}
