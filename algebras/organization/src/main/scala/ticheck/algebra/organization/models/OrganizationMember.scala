package ticheck.algebra.organization.models

import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.dao.organization.membership.{JoinedAt, OrganizationRole}
import ticheck.dao.user.models.UserRecord
import ticheck.{Name, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationMember(
  userId:   UserID,
  name:     Name,
  role:     OrganizationRole,
  joinedAt: JoinedAt,
)

object OrganizationMember {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationMember] = derive.codec[OrganizationMember]

  def fromDAO(u: UserRecord, om: OrganizationMembershipRecord): OrganizationMember =
    OrganizationMember(
      u.id,
      u.name,
      om.role,
      om.joinedAt,
    )

}
