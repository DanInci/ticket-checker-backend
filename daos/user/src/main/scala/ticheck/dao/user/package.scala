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

  type Email = Email.Type

  object HashedPassword extends PhantomType[String]
  type HashedPassword = HashedPassword.Type

  object Name extends PhantomType[String]
  type Name = Name.Type

  object EditedAt extends PhantomType[OffsetDateTime]
  type EditedAt = EditedAt.Type

}
