package ticheck.dao.organization

import java.time.OffsetDateTime

import busymachines.pureharm.phantom.PhantomType

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
package object invite {

  object InviteCode extends PhantomType[String]
  type InviteCode = InviteCode.Type

  object AnsweredAt extends PhantomType[OffsetDateTime]
  type AnsweredAt = AnsweredAt.Type

  object InvitedAt extends PhantomType[OffsetDateTime]
  type InvitedAt = InvitedAt.Type

}
