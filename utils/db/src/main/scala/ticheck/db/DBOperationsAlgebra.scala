package ticheck.db

import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait DBOperationsAlgebra[F[_]] {

  protected def transact[A](
    query:      ConnectionIO[A],
  )(implicit F: Sync[F], transactor: Transactor[F]): F[A] = {
    query.transact(transactor)
  }

}
