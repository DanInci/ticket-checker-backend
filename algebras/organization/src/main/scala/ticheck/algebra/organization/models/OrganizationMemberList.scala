package ticheck.algebra.organization.models

import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.{Name, OrganizationID, UserID}
import ticheck.dao.organization.membership.{JoinedAt, OrganizationRole}
import ticheck.dao.user.models.UserRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationMemberList(
  organizationId: OrganizationID,
  userId:         UserID,
  name:           Name,
  role:           OrganizationRole,
  joinedAt:       JoinedAt,
)

object OrganizationMemberList {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationMemberList] = derive.codec[OrganizationMemberList]

  def fromDAO(om: OrganizationMembershipRecord, u: UserRecord): OrganizationMemberList =
    OrganizationMemberList(
      om.organizationId,
      u.id,
      u.name,
      om.role,
      om.joinedAt,
    )

}
