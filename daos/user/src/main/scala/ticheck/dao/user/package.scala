package ticheck.dao

import java.time.OffsetDateTime

import ticheck.PhantomType
import ticheck.PhantomFUUID

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
package object user {

  object UserID extends PhantomFUUID
  type UserID = UserID.Type

  type Email = Email.Type

  object HashedPassword extends PhantomType[String]
  type HashedPassword = HashedPassword.Type

  object Name extends PhantomType[String]
  type Name = Name.Type

  object EditedAt extends PhantomType[OffsetDateTime]
  type EditedAt = EditedAt.Type

}
