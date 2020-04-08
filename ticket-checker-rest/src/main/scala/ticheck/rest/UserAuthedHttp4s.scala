package ticheck.rest

import ticheck.auth.models.UserAuthCtx
import ticheck.auth.{AuthAlgebra, JWTAuthConfig}
import ticheck.effect._
import ticheck.auth.http.AuthedHttp4s
import ticheck.auth.models.RawAuthCtx

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class UserAuthedHttp4s[F[_]] private (
  authAlgebra:             AuthAlgebra[F],
)(implicit override val S: Sync[F])
    extends AuthedHttp4s[F, RawAuthCtx, UserAuthCtx] {

  override protected def convertContext(ctx: RawAuthCtx): F[UserAuthCtx] =
    authAlgebra.convert(ctx)

}

object UserAuthedHttp4s {

  def sync[F[_]: Sync](
    jwtAuthConfig: JWTAuthConfig,
    authAlgebra:   AuthAlgebra[F],
  ): F[UserCtxMiddleware[F]] = Sync[F].delay {
    val authedHttp4s = new UserAuthedHttp4s[F](authAlgebra)
    authedHttp4s.authMiddleware(jwtAuthConfig)
  }

}
