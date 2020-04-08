package ticheck.organizer.user.impl

import ticheck.UserID
import ticheck.algebra.user.UserAlgebra
import ticheck.auth.models.UserAuthCtx
import ticheck.algebra.user.models._
import ticheck.auth.AuthAlgebra
import ticheck.auth.models.{LoginRequest, RegistrationRequest}
import ticheck.organizer.user.UserOrganizer
import ticheck.organizer.user.models.LoginResponse
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[user] class UserOrganizerImpl[F[_]](
  private val authAlgebra: AuthAlgebra[F],
  private val userAlgebra: UserAlgebra[F],
)(implicit F:              Sync[F])
    extends UserOrganizer[F] {

  override def register(regData: RegistrationRequest): F[Unit] = authAlgebra.register(regData)

  override def login(loginData: LoginRequest): F[LoginResponse] =
    for {
      token   <- authAlgebra.login(loginData)
      profile <- userAlgebra.getProfileByEmail(loginData.email)
    } yield LoginResponse(token, profile)

  override def getUserProfile(id: UserID)(implicit ctx: UserAuthCtx): F[UserProfile] =
    for {
      profile <- userAlgebra.getProfileById(id)
    } yield profile

  override def updateUserProfile(id: UserID, definition: UserDefinition)(implicit ctx: UserAuthCtx): F[UserProfile] =
    for {
      profile <- userAlgebra.updateById(id, definition)
    } yield profile

  override def deleteUser(id: UserID)(implicit ctx: UserAuthCtx): F[Unit] =
    for {
      _ <- userAlgebra.deleteById(id)
    } yield ()

}
