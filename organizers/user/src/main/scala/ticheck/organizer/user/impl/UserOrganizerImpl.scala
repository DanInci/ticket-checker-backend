package ticheck.organizer.user.impl

import ticheck.UserID
import ticheck.algebra.user.UserAlgebra
import ticheck.auth.models.UserAuthCtx
import ticheck.algebra.user.models._
import ticheck.auth.AuthAlgebra
import ticheck.auth.models.{LoginRequest, RegistrationRequest}
import ticheck.organizer.user.UserOrganizer
import ticheck.organizer.user.models.LoginResponse

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[user] class UserOrganizerImpl[F[_]](
  private val authAlgebra: AuthAlgebra[F],
  private val userAlgebra: UserAlgebra[F],
) extends UserOrganizer[F] {

  override def register(regData: RegistrationRequest): F[Unit] = ???

  override def login(loginData: LoginRequest): F[LoginResponse] = ???

  override def getUserProfile(id: UserID)(implicit ctx: UserAuthCtx): F[UserProfile] = ???

  override def updateUserProfile(id: UserID, definition: UserDefinition)(implicit ctx: UserAuthCtx): F[UserProfile] =
    ???

  override def deleteUser(id: UserID)(implicit ctx: UserAuthCtx): F[Unit] = ???

}
