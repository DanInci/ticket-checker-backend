package ticheck.dao.organization.membership.models

import ticheck._
import ticheck.dao.organization.membership._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class OrganizationMembershipRecord(
  id:             OrganizationMembershipID,
  userId:         UserID,
  organizationId: OrganizationID,
  inviteId:       Option[OrganizationInviteID],
  role:           OrganizationRole,
  joinedAt:       JoinedAt,
)
