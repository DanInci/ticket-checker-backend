package ticheck.dao.user

import ticheck.db.ConnectionIO
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleUserDAO[F[_]] {

  protected def F: Sync[F]

  def userSQL: F[UserSQL[ConnectionIO]] = _userSql

  private lazy val _userSql: F[UserSQL[ConnectionIO]] = F.pure(impl.UserSQLImpl)

}
