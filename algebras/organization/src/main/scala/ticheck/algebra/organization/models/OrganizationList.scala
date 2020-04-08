package ticheck.algebra.organization.models

import ticheck.{CreatedAt, OrganizationID}
import ticheck.dao.organization.OrganizationName

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationList(
  id:        OrganizationID,
  name:      OrganizationName,
  createdAt: CreatedAt,
)

object OrganizationList {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationList] = derive.codec[OrganizationList]

}
