package ticheck.dao.organization.invite.models

import ticheck.dao.organization.invite._
import ticheck._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class OrganizationInviteRecord(
  id:             OrganizationInviteID,
  organizationId: OrganizationID,
  email:          Email,
  code:           InviteCode,
  status:         InviteStatus,
  respondedAt:    RespondedAt,
  invitedAt:      InvitedAt,
)
