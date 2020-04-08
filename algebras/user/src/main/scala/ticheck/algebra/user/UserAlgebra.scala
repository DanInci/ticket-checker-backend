package ticheck.algebra.user

import ticheck.UserID
import ticheck.algebra.user.models.UserProfile

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait UserAlgebra[F[_]] {

  def getById(id: UserID): F[UserProfile]

}
