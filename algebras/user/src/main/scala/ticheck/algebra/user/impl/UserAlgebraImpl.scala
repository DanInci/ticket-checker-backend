package ticheck.algebra.user.impl

import ticheck.UserID
import ticheck.algebra.user.models.UserProfile
import ticheck.algebra.user.{UserAlgebra, UserModuleAlgebra}
import ticheck.dao.user.UserSQL
import ticheck.effect._
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[user] class UserAlgebraImpl[F[_]] private (
  userSQL:    UserSQL[ConnectionIO],
)(implicit F: Async[F], transactor: Transactor[F])
    extends UserAlgebra[F] with DBOperationsAlgebra[F] {

  override def getProfileById(id: UserID): F[UserProfile] = ???

}

private[user] object UserAlgebraImpl {

  def async[F[_]: Async: Transactor](userSQL: UserSQL[ConnectionIO]): F[UserModuleAlgebra[F]] =
    Async[F].pure(new UserAlgebraImpl[F](userSQL))

}
