package ticheck.organizer.user.impl

import ticheck.UserID
import ticheck.effect._
import ticheck.algebra.user.UserAlgebra
import ticheck.algebra.user.models.auth.UserAuthCtx
import ticheck.algebra.user.models._
import ticheck.organizer.user.UserOrganizer

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[user] class UserOrganizerImpl[F[_]](
  private val userAlgebra: UserAlgebra[F],
) extends UserOrganizer[F] {

  override def register(regData: UserRegistration): F[Unit] = ???

  override def login(loginData: UserLoginRequest): F[UserLoginResponse] = ???

  override def getUserProfile(id: UserID)(implicit ctx: UserAuthCtx): F[UserProfile] = ???

  override def updateUserProfile(id: UserID, definition: UserDefinition)(implicit ctx: UserAuthCtx): F[UserProfile] =
    ???

  override def deleteUser(id: UserID)(implicit ctx: UserAuthCtx): F[Unit] = ???

}
