package ticheck.algebra.organization.models

import ticheck.dao.organization.membership.OrganizationRole

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationMemberDefinition(
  role: OrganizationRole,
)

object OrganizationMemberDefinition {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationMemberDefinition] = derive.codec[OrganizationMemberDefinition]

}
