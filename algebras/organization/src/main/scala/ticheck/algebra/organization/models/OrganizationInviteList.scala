package ticheck.algebra.organization.models

import ticheck.{OrganizationID, OrganizationInviteID}
import ticheck.dao.organization.OrganizationName
import ticheck.dao.organization.invite.models.OrganizationInviteRecord
import ticheck.dao.organization.invite.{InviteStatus, InvitedAt}
import ticheck.dao.organization.models.OrganizationRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/10/2020
  *
  */
final case class OrganizationInviteList(
  id:               OrganizationInviteID,
  organizationId:   OrganizationID,
  organizationName: OrganizationName,
  status:           InviteStatus,
  invitedAt:        InvitedAt,
)

object OrganizationInviteList {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationInviteList] = derive.codec[OrganizationInviteList]

  def fromDAO(oi: OrganizationInviteRecord, o: OrganizationRecord): OrganizationInviteList =
    OrganizationInviteList(
      oi.id,
      o.id,
      o.name,
      oi.status,
      oi.invitedAt,
    )

}
