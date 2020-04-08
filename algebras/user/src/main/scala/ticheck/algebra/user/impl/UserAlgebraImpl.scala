package ticheck.algebra.user.impl

import ticheck._
import ticheck.algebra.user.models._
import ticheck.algebra.user._
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

  override def getProfileByEmail(email: Email): F[UserProfile] = ???

  override def updateById(id: UserID, definition: UserDefinition): F[UserProfile] = ???

  override def deleteById(id: UserID): F[Unit] = ???

}

private[user] object UserAlgebraImpl {

  def async[F[_]: Async: Transactor](userSQL: UserSQL[ConnectionIO]): F[UserModuleAlgebra[F]] =
    Async[F].pure(new UserAlgebraImpl[F](userSQL))

}
