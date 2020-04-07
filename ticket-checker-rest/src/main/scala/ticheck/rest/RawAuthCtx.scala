package ticheck.rest

import ticheck.auth.AuthCtx
import ticheck.{OrganizationID, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
case class RawAuthCtx(
  userId:          UserID,
  organizationIds: List[OrganizationID],
) extends AuthCtx

object RawAuthCtx {
  import ticheck.json._

  implicit val jsonCodec: Codec[RawAuthCtx] = derive.codec[RawAuthCtx]
}
