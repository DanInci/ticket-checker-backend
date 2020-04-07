package ticheck.organizer.user

import ticheck.effect._
import ticheck.algebra.user.ModuleUserAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleUserOrganizer[F[_]] { this: ModuleUserAlgebra[F] =>

  implicit protected def F: Async[F]

  def userOrganizer: F[UserOrganizer[F]] = ???

}
