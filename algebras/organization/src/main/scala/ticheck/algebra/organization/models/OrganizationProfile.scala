package ticheck.algebra.organization.models

import ticheck.{CreatedAt, OrganizationID}
import ticheck.dao.organization._
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.dao.organization.models.OrganizationRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class OrganizationProfile(
  id:         OrganizationID,
  name:       OrganizationName,
  membership: Option[Membership],
  createdAt:  CreatedAt,
)

object OrganizationProfile {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationProfile] = derive.codec[OrganizationProfile]

  def fromDAO(o: OrganizationRecord, om: Option[OrganizationMembershipRecord]): OrganizationProfile =
    OrganizationProfile(
      o.id,
      o.name,
      om.map(Membership.fromDAO),
      o.createdAt,
    )

}
