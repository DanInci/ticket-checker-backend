package ticheck.rest

import ticheck.algebra.organization.OrganizationAlgebra
import ticheck.algebra.user.UserAlgebra
import ticheck.auth.JWTAuthConfig
import ticheck.effect._
import ticheck.auth.http.AuthedHttp4s

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class UserAuthedHttp4s[F[_]] private (
  userAlgebra:             UserAlgebra[F],
  organizationAlgebra:     OrganizationAlgebra[F],
)(implicit override val S: Sync[F])
    extends AuthedHttp4s[F, RawAuthCtx, UserAuthCtx] {

  override protected def convertContext(ctx: RawAuthCtx): F[UserAuthCtx] =
    for {
      user          <- userAlgebra.getById(ctx.userId)
      organizations <- ctx.organizationIds.traverse(organizationAlgebra.getById)
    } yield UserAuthCtx(user, organizations)

}

object UserAuthedHttp4s {

  def sync[F[_]: Sync](
    jwtAuthConfig:       JWTAuthConfig,
    userAlgebra:         UserAlgebra[F],
    organizationAlgebra: OrganizationAlgebra[F],
  ): F[UserCtxMiddleware[F]] = Sync[F].delay {
    val authedHttp4s = new UserAuthedHttp4s[F](userAlgebra, organizationAlgebra)
    authedHttp4s.authMiddleware(jwtAuthConfig)
  }

}
