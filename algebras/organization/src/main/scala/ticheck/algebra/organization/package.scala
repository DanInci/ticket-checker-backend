package ticheck.algebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
package object organization {

  type OrganizationModuleAlgebra[F[_]] = OrganizationAlgebra[F] with OrganizationStatisticsAlgebra[F]

}
