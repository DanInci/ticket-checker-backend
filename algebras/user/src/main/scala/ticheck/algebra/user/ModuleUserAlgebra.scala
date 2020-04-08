package ticheck.algebra.user

import ticheck.db.Transactor
import ticheck.dao.user.ModuleUserDAO
import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleUserAlgebra[F[_]] { this: ModuleUserDAO[F] with ModuleTimeAlgebra[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def userModuleAlgebra: F[UserModuleAlgebra[F]] = _userModuleAlgebra

  private lazy val _userModuleAlgebra: F[UserModuleAlgebra[F]] =
    for {
      usql <- userSQL
      ua   <- impl.UserAlgebraImpl.async(usql)
    } yield ua

}
