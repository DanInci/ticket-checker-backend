package ticheck.algebra

import ticheck.PhantomType

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
package object user {

  object PlainTextPassword extends PhantomType[String]
  type PlainTextPassword = PlainTextPassword.Type

}
