package ticheck.organizer.user

import ticheck.UserID
import ticheck.algebra.user.UserAlgebra
import ticheck.algebra.user.models.auth.UserAuthCtx
import ticheck.algebra.user.models._
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait UserOrganizer[F[_]] {

  def register(regData: UserRegistration): F[Unit]

  def login(loginData: UserLoginRequest): F[UserLoginResponse]

  def getUserProfile(id: UserID)(implicit ctx: UserAuthCtx): F[UserProfile]

  def updateUserProfile(id: UserID, definition: UserDefinition)(implicit ctx: UserAuthCtx): F[UserProfile]

  def deleteUser(id: UserID)(implicit ctx: UserAuthCtx): F[Unit]

}

object UserOrganizer {

  def apply[F[_]: Sync](userAlgebra: UserAlgebra[F]): F[UserOrganizer[F]] = Sync[F].pure(
    new impl.UserOrganizerImpl[F](userAlgebra),
  )

}
