package ticheck.algebra.organization.models

import ticheck.{Email, OrganizationInviteID}
import ticheck.dao.organization.invite.{InviteStatus, InvitedAt, RespondedAt}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationInvite(
  id:          OrganizationInviteID,
  email:       Email,
  status:      InviteStatus,
  invitedAt:   InvitedAt,
  respondedAt: RespondedAt,
)

object OrganizationInvite {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationInvite] = derive.codec[OrganizationInvite]
}
