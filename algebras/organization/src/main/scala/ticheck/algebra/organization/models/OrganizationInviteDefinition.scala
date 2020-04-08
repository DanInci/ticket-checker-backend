package ticheck.algebra.organization.models

import ticheck.Email

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationInviteDefinition(
  email: Email,
)

object OrganizationInviteDefinition {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationInviteDefinition] = derive.codec[OrganizationInviteDefinition]
}
