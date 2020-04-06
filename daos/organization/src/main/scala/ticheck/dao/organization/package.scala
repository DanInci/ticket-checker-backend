package ticheck.dao

import ticheck._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
package object organization {

  object OrganizationName extends PhantomType[String]
  type OrganizationName = OrganizationName.Type

}
