package ticheck.algebra.user

import ticheck.{Email, UserID}
import ticheck.algebra.user.models.{UserDefinition, UserProfile}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait UserAlgebra[F[_]] {

  def getProfileById(id: UserID): F[UserProfile]

  def getProfileByEmail(email: Email): F[UserProfile]

  def updateById(id: UserID, definition: UserDefinition): F[UserProfile]

  def deleteById(id: UserID): F[Unit]

}
