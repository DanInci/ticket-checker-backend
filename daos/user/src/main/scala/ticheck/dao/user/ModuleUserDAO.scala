package ticheck.dao.user

import ticheck.db.ConnectionIO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleUserDAO[F[_]] { this: ModuleTimeAlgebra[F] =>

  protected def F: Sync[F]

  def userSQL: F[UserSQL[ConnectionIO]] = _userSql

  private lazy val _userSql: F[UserSQL[ConnectionIO]] =
    timeAlgebra.flatMap(t => impl.UserSQLImpl.sync(t))

}
