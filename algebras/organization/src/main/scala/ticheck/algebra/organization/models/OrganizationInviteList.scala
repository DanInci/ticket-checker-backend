package ticheck.algebra.organization.models

import ticheck.OrganizationInviteID
import ticheck.dao.organization.invite.models.OrganizationInviteRecord
import ticheck.dao.organization.invite.{InviteStatus, InvitedAt}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/10/2020
  *
  */
final case class OrganizationInviteList(
  id:        OrganizationInviteID,
  status:    InviteStatus,
  invitedAt: InvitedAt,
)

object OrganizationInviteList {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationInviteList] = derive.codec[OrganizationInviteList]

  def fromDAO(oi: OrganizationInviteRecord): OrganizationInviteList =
    OrganizationInviteList(
      oi.id,
      oi.status,
      oi.invitedAt,
    )

}
