package ticheck.algebra.organization.models

import ticheck.dao.organization.OrganizationName
import ticheck.dao.organization.invite.models.OrganizationInviteRecord
import ticheck.{Email, OrganizationID, OrganizationInviteID}
import ticheck.dao.organization.invite.{AnsweredAt, InviteStatus, InvitedAt}
import ticheck.dao.organization.models.OrganizationRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationInvite(
  id:               OrganizationInviteID,
  organizationId:   OrganizationID,
  organizationName: OrganizationName,
  email:            Email,
  status:           InviteStatus,
  answeredAt:       Option[AnsweredAt],
  invitedAt:        InvitedAt,
)

object OrganizationInvite {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationInvite] = derive.codec[OrganizationInvite]

  def fromDAO(oi: OrganizationInviteRecord, o: OrganizationRecord): OrganizationInvite =
    OrganizationInvite(
      oi.id,
      o.id,
      o.name,
      oi.email,
      oi.status,
      oi.answeredAt,
      oi.invitedAt,
    )
}
