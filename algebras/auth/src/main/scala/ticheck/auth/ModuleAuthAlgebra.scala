package ticheck.auth

import ticheck.dao.user.ModuleUserDAO
import ticheck.effect._
import ticheck.db.Transactor

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
trait ModuleAuthAlgebra[F[_]] { this: ModuleUserDAO[F] =>

  implicit protected def F: Async[F]

  implicit protected def transactor: Transactor[F]

  def authAlgebra: F[AuthAlgebra[F]] = _authAlgebra

  private lazy val _authAlgebra: F[AuthAlgebra[F]] =
    for {
      usql <- userSQL
      au   <- impl.AuthAlgebraImpl.async(usql)
    } yield au

}
