package ticheck.organizer.organization

import ticheck.effect.Async
import ticheck.algebra.organization.ModuleOrganizationAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ModuleOrganizationOrganizer[F[_]] { this: ModuleOrganizationAlgebra[F] =>

  implicit protected def F: Async[F]

}
