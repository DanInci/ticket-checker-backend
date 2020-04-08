package ticheck.auth

import ticheck.dao.user.HashedPassword
import ticheck.effect._
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.BCrypt

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
private[auth] object BCryptPasswordHash {

  def forPassword[F[_]: Sync](ptp: PlainTextPassword): F[HashedPassword] =
    BCrypt.hashpw[F](PlainTextPassword.despook(ptp)).map(HashedPassword.spook)

  /**
    * Checks if the given plain text password matches the given
    * BCrypt hash
    */
  def check[F[_]: Sync](ptp: PlainTextPassword, hash: HashedPassword): F[Boolean] =
    BCrypt.checkpwBool[F](ptp, PasswordHash.apply(hash))

}
