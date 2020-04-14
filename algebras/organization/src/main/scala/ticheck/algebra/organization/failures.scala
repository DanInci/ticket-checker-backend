package ticheck.algebra.organization

import busymachines.pureharm.anomaly.{ConflictAnomaly, InvalidInputAnomaly}
import ticheck.dao.organization.OrganizationName
import ticheck.dao.organization.invite.{InviteCode, InviteStatus}
import ticheck.dao.organization.membership.OrganizationRole
import ticheck.{Anomaly, AnomalyID, AnomalyIDs, Email, NotFoundAnomaly, OrganizationID, OrganizationInviteID, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/9/2020
  *
  */
final case class OrganizationNFA(organizationId: OrganizationID)
    extends NotFoundAnomaly(
      s"Organization with id '$organizationId' was not found",
    ) {
  override val id: AnomalyID = AnomalyIDs.OrganizationNotFoundID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "organizationId" -> organizationId.show,
  )
}

final case class OrganizationAlreadyExistsCA(name: OrganizationName)
    extends InvalidInputAnomaly(s"Organization with name '$name' already exists") {
  override val id: AnomalyID = AnomalyIDs.OrganizationAlreadyExistsID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "name" -> OrganizationName.despook(name),
  )
}

final case class OrganizationInviteForEmailExistsCA(organizationId: OrganizationID, email: Email)
    extends ConflictAnomaly(
      s"Organization with id '$organizationId' has an invite for user with email'$email' already",
    ) {
  override val id: AnomalyID = AnomalyIDs.OrganizationInviteExistsID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "organizationId" -> organizationId.show,
    "email"          -> Email.despook(email),
  )
}

final case class OrganizationMembershipForEmailExistsCA(organizationId: OrganizationID, email: Email)
    extends ConflictAnomaly(
      s"Organization with id '$organizationId' has a member with email'$email' already",
    ) {
  override val id: AnomalyID = AnomalyIDs.OrganizationMemberExistsID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "organizationId" -> organizationId.show,
    "email"          -> Email.despook(email),
  )
}

final case class OrganizationInviteNFA private (inviteId: Option[OrganizationInviteID], code: Option[InviteCode])
    extends NotFoundAnomaly(
      s"Organization invite with ${if (inviteId.isDefined) "id" else "code"} '${if (inviteId.isDefined) inviteId.get.show
      else code.get}' was not found",
    ) {

  def this(inviteId: OrganizationInviteID) { this(Some(inviteId), None) }
  def this(code:     InviteCode) { this(None, Some(code)) }

  override val id: AnomalyID = AnomalyIDs.OrganizationInviteNotFoundID
  override val parameters: Anomaly.Parameters = (inviteId, code) match {
    case (Some(inviteId), None) =>
      Anomaly.Parameters(
        "inviteId" -> inviteId.show,
      )
    case (None, Some(code)) =>
      Anomaly.Parameters(
        "code" -> InviteCode.despook(code),
      )
    case _ => Anomaly.Parameters()
  }
}

final case class OrganizationInviteIsAnsweredCA(
  inviteId: Option[OrganizationInviteID],
  code:     Option[InviteCode],
  status:   InviteStatus,
) extends ConflictAnomaly(
      s"Organization invite with ${if (inviteId.isDefined) "id" else "code"} '${if (inviteId.isDefined) inviteId.get.show
      else code.get}' is already answered",
    ) {

  def this(inviteId: OrganizationInviteID, status: InviteStatus) { this(Some(inviteId), None, status) }
  def this(code:     InviteCode, status:           InviteStatus) { this(None, Some(code), status) }

  override val id: AnomalyID = AnomalyIDs.OrganizationInviteIsAnsweredID

  override val parameters: Anomaly.Parameters = (inviteId, code) match {
    case (Some(inviteId), None) =>
      Anomaly.Parameters(
        "inviteId" -> inviteId.show,
        "status"   -> status.asString,
      )
    case (None, Some(code)) =>
      Anomaly.Parameters(
        "code"   -> InviteCode.despook(code),
        "status" -> status.asString,
      )
    case _ =>
      Anomaly.Parameters(
        "status" -> status.asString,
      )
  }

}

final case class OrganizationMemberNFA(organizationId: OrganizationID, userId: UserID)
    extends NotFoundAnomaly(
      s"Organization membership for organization with id '$organizationId' and user with id '$userId' was not found",
    ) {
  override val id: AnomalyID = AnomalyIDs.OrganizationMemberNotFoundID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "organizationId" -> organizationId.show,
    "userId"         -> userId.show,
  )
}

final case class OrganizationMemberChangesNotAllowedIIA(
  organizationId: OrganizationID,
  userId:         UserID,
) extends InvalidInputAnomaly(
      s"You are not allow to make changes to user's with id '$userId' who is a member of organization with id '$organizationId'",
    ) {
  override val id: AnomalyID = AnomalyIDs.OrganizationMemberChangesNotAllowedID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "organizationId" -> organizationId.show,
    "userId"         -> userId.show,
  )
}

final case class OrganizationMemberRoleNotAllowedIIA(
  organizationId: OrganizationID,
  userId:         UserID,
  role:           OrganizationRole,
) extends InvalidInputAnomaly(
      s"You are not allow to set user's with id '$userId' role to '${role.asString}' for organization with id '$organizationId'",
    ) {
  override val id: AnomalyID = AnomalyIDs.OrganizationMemberRoleNotAllowedID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "organizationId" -> organizationId.show,
    "userId"         -> userId.show,
    "role"           -> role.asString,
  )
}
