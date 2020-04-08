package ticheck.auth.impl

import ticheck.auth.{AuthAlgebra, JWTAuthToken}
import ticheck.auth.models.{LoginRequest, RawAuthCtx, RegistrationRequest, UserAuthCtx}
import ticheck.dao.user.UserSQL
import ticheck.db._
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[auth] class AuthAlgebraImpl[F[_]] private (userSQL: UserSQL[ConnectionIO])(
  implicit F:                                                     Async[F],
  transactor:                                                     Transactor[F],
) extends AuthAlgebra[F] {

  override def convert(rawAuthCtx: RawAuthCtx): F[UserAuthCtx] = ???

  override def register(regData: RegistrationRequest): F[Unit] = ???

  override def login(loginData: LoginRequest): F[JWTAuthToken] = ???

}

private[auth] object AuthAlgebraImpl {

  def async[F[_]: Async: Transactor](userSQL: UserSQL[ConnectionIO]): F[AuthAlgebra[F]] =
    Async[F].pure(new AuthAlgebraImpl[F](userSQL))

}
