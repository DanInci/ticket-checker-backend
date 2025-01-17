package ticheck.auth

import ticheck.auth.models.{LoginRequest, RawAuthCtx, RegistrationRequest, UserAuthCtx}
import ticheck.dao.user.VerificationCode

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
trait AuthAlgebra[F[_]] {

  def convert(rawAuthCtx: RawAuthCtx): F[UserAuthCtx]

  def register(regData: RegistrationRequest): F[Unit]

  def verify(code: VerificationCode): F[Unit]

  def login(loginData: LoginRequest): F[JWTAuthToken]

}
