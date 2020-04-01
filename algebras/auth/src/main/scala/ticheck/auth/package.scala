package ticheck

import scala.concurrent.duration.FiniteDuration

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
package object auth {

  object RawBytesSharedSecretKey extends PhantomType[Array[Byte]]
  type RawBytesSharedSecretKey = RawBytesSharedSecretKey.Type

  object RawStringSharedSecretKey extends PhantomType[String]
  type RawStringSharedSecretKey = RawStringSharedSecretKey.Type

  object JWTTokenExpirationDuration extends PhantomType[FiniteDuration]
  type JWTTokenExpirationDuration = JWTTokenExpirationDuration.Type

}
