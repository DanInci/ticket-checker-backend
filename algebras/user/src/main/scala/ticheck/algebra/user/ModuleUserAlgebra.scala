package ticheck.algebra.user

import ticheck.effect._
import ticheck.time.ModuleTimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleUserAlgebra[F[_]] { this: ModuleTimeAlgebra[F] =>

  implicit protected def F: Async[F]

}
