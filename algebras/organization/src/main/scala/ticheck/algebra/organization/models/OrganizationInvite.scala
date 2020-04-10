package ticheck.algebra.organization.models

import ticheck.dao.organization.invite.models.OrganizationInviteRecord
import ticheck.{Email, OrganizationInviteID}
import ticheck.dao.organization.invite.{AnsweredAt, InviteStatus, InvitedAt}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationInvite(
  id:         OrganizationInviteID,
  email:      Email,
  status:     InviteStatus,
  answeredAt: Option[AnsweredAt],
  invitedAt:  InvitedAt,
)

object OrganizationInvite {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationInvite] = derive.codec[OrganizationInvite]

  def fromDAO(oi: OrganizationInviteRecord): OrganizationInvite =
    OrganizationInvite(
      oi.id,
      oi.email,
      oi.status,
      oi.answeredAt,
      oi.invitedAt,
    )
}
