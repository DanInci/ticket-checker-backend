package ticheck.auth

import ticheck.dao.user.HashedPassword
import ticheck.effect._
import com.github.t3hnar.bcrypt._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
private[auth] object BCryptPasswordHash {

  def forPassword[F[_]: Sync](ptp: PlainTextPassword): F[HashedPassword] =
    Sync[F].delay(ptp.bcrypt).map(HashedPassword.spook)

  /**
    * Checks if the given plain text password matches the given
    * BCrypt hash
    */
  def check[F[_]: Sync](ptp: PlainTextPassword, hash: HashedPassword): F[Boolean] =
    Sync[F].delay(ptp.isBcryptedSafe(hash).getOrElse(false))

}
