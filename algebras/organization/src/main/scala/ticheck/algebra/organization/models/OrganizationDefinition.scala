package ticheck.algebra.organization.models

import ticheck.dao.organization.OrganizationName

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationDefinition(
  name: OrganizationName,
)

object OrganizationDefinition {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationDefinition] = derive.codec[OrganizationDefinition]
}
