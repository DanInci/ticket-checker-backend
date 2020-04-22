package ticheck.algebra.organization.models

import ticheck.dao.organization.OrganizationName
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.dao.organization.membership.{JoinedAt, OrganizationRole, SoldTicketsNo, ValidatedTicketsNo}
import ticheck.dao.organization.models.OrganizationRecord
import ticheck.dao.user.models.UserRecord
import ticheck.{Name, OrganizationID, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationMember(
  organizationId:     OrganizationID,
  organizationName:   OrganizationName,
  userId:             UserID,
  name:               Name,
  role:               OrganizationRole,
  joinedAt:           JoinedAt,
  soldTicketsNo:      SoldTicketsNo,
  validatedTicketsNo: ValidatedTicketsNo,
)

object OrganizationMember {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationMember] = derive.codec[OrganizationMember]

  def fromDAO(
    o:                  OrganizationRecord,
    u:                  UserRecord,
    om:                 OrganizationMembershipRecord,
    soldTicketsNo:      SoldTicketsNo,
    validatedTicketsNo: ValidatedTicketsNo,
  ): OrganizationMember =
    OrganizationMember(
      o.id,
      o.name,
      u.id,
      u.name,
      om.role,
      om.joinedAt,
      soldTicketsNo,
      validatedTicketsNo,
    )

}
