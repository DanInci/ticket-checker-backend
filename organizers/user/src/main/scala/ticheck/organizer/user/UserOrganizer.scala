package ticheck.organizer.user

import ticheck.UserID
import ticheck.algebra.user.UserAlgebra
import ticheck.auth.models.UserAuthCtx
import ticheck.algebra.user.models._
import ticheck.auth.AuthAlgebra
import ticheck.auth.models.{LoginRequest, RegistrationRequest}
import ticheck.effect.Sync
import ticheck.organizer.user.models.LoginResponse

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait UserOrganizer[F[_]] {

  def register(regData: RegistrationRequest): F[Unit]

  def login(loginData: LoginRequest): F[LoginResponse]

  def getUserProfile(id: UserID)(implicit ctx: UserAuthCtx): F[UserProfile]

  def updateUserProfile(id: UserID, definition: UserDefinition)(implicit ctx: UserAuthCtx): F[UserProfile]

  def deleteUser(id: UserID)(implicit ctx: UserAuthCtx): F[Unit]

}

object UserOrganizer {

  def apply[F[_]: Sync](authAlgebra: AuthAlgebra[F], userAlgebra: UserAlgebra[F]): F[UserOrganizer[F]] = Sync[F].pure(
    new impl.UserOrganizerImpl[F](authAlgebra, userAlgebra),
  )

}
