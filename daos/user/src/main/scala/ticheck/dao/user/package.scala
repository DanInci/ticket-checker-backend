package ticheck.dao

import java.time.OffsetDateTime

import ticheck.PhantomType

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
package object user {

  object HashedPassword extends PhantomType[String]
  type HashedPassword = HashedPassword.Type

  object EditedAt extends PhantomType[OffsetDateTime]
  type EditedAt = EditedAt.Type

}
