package ticheck.organizer.organization

import ticheck.{OrganizationID, OrganizationInviteID, PagingInfo, UserID}
import ticheck.algebra.organization.OrganizationAlgebra
import ticheck.algebra.organization.models._
import ticheck.auth.models.UserAuthCtx
import ticheck.dao.organization.invite.{InviteCode, InviteStatus}
import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait OrganizationOrganizer[F[_]] {

  def getOrganizationList(pagingInfo: PagingInfo)(implicit ctx: UserAuthCtx): F[List[OrganizationList]]

  def registerOrganization(definition: OrganizationDefinition)(implicit ctx: UserAuthCtx): F[OrganizationProfile]

  def getOrganizationProfile(id: OrganizationID)(implicit ctx: UserAuthCtx): F[OrganizationProfile]

  def updateOrganizationProfile(id: OrganizationID, definition: OrganizationDefinition)(
    implicit ctx:                   UserAuthCtx,
  ): F[OrganizationProfile]

  def deleteOrganization(id: OrganizationID)(implicit ctx: UserAuthCtx): F[Unit]

  def getOrganizationInvites(id: OrganizationID, pagingInfo: PagingInfo, statusFilter: Option[InviteStatus])(
    implicit ctx:                UserAuthCtx,
  ): F[List[OrganizationInviteList]]

  def invite(id: OrganizationID, invite: OrganizationInviteDefinition)(implicit ctx: UserAuthCtx): F[OrganizationInvite]

  def cancelInvite(id: OrganizationID, inviteId: OrganizationInviteID)(implicit ctx: UserAuthCtx): F[Unit]

  def join(code: InviteCode)(implicit ctx: UserAuthCtx): F[OrganizationProfile]

  def setInviteStatus(id: OrganizationID, inviteId: OrganizationInviteID, status: InviteStatus)(
    implicit ctx:         UserAuthCtx,
  ): F[OrganizationProfile]

  def getOrganizationMemberList(id: OrganizationID, pagingInfo: PagingInfo)(
    implicit ctx:                   UserAuthCtx,
  ): F[List[OrganizationMemberList]]

  def getOrganizationMemberById(id: OrganizationID, userId: UserID)(implicit ctx: UserAuthCtx): F[OrganizationMember]

  def updateOrganizationMember(
    id:           OrganizationID,
    userId:       UserID,
    definition:   OrganizationMemberDefinition,
  )(implicit ctx: UserAuthCtx): F[OrganizationMember]

  def removeOrganizationMember(id: OrganizationID, userId: UserID)(implicit ctx: UserAuthCtx): F[Unit]

}

object OrganizationOrganizer {

  def apply[F[_]: Sync](organizationAlgebra: OrganizationAlgebra[F]): F[OrganizationOrganizer[F]] = Sync[F].pure(
    new impl.OrganizationOrganizerImpl[F](organizationAlgebra),
  )

}
