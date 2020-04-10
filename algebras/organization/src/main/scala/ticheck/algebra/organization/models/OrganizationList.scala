package ticheck.algebra.organization.models

import ticheck.{CreatedAt, OrganizationID}
import ticheck.dao.organization.OrganizationName
import ticheck.dao.organization.models.OrganizationRecord

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

  def fromDAO(o: OrganizationRecord): OrganizationList =
    OrganizationList(
      o.id,
      o.name,
      o.createdAt,
    )

}
