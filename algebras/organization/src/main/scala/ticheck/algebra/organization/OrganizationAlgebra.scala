package ticheck.algebra.organization

import ticheck.{Email, OrganizationID, OrganizationInviteID, PagingInfo, UserID}
import ticheck.algebra.organization.models._
import ticheck.dao.organization.invite.{InviteCode, InviteStatus}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationAlgebra[F[_]] {

  def getAll(filter: Option[List[OrganizationID]], pagingInfo: PagingInfo): F[List[OrganizationList]]

  def create(definition: OrganizationDefinition)(implicit createdBy: UserID): F[OrganizationProfile]

  def getById(id: OrganizationID): F[OrganizationProfile]

  def updateById(id: OrganizationID, definition: OrganizationDefinition): F[OrganizationProfile]

  def deleteById(id: OrganizationID): F[Unit]

  def sendInvite(id: OrganizationID, invite: OrganizationInviteDefinition): F[OrganizationInvite]

  def cancelInvite(id: OrganizationID, inviteId: OrganizationInviteID): F[Unit]

  def join(inviteCode: InviteCode)(implicit userId: UserID, email: Email): F[OrganizationProfile]

  def setInviteStatus(id: OrganizationID, inviteId: OrganizationInviteID, status: InviteStatus)(
    implicit userId:      UserID,
    email:                Email,
  ): F[Unit]

  def getMembersList(id: OrganizationID, pagingInfo: PagingInfo): F[List[OrganizationMemberList]]

  def updateMemberByUserID(
    id:         OrganizationID,
    userId:     UserID,
    definition: OrganizationMemberDefinition,
  ): F[OrganizationMember]

  def removeMemberByUserID(id: OrganizationID, userId: UserID): F[Unit]

}
