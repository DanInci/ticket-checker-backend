package ticheck.organizer.organization.impl

import ticheck.{OrganizationID, OrganizationInviteID, PagingInfo, UserID}
import ticheck.algebra.organization.OrganizationAlgebra
import ticheck.algebra.organization.models._
import ticheck.auth.models.UserAuthCtx
import ticheck.dao.organization.invite.{InviteCode, InviteStatus}
import ticheck.organizer.organization.OrganizationOrganizer
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[organization] case class OrganizationOrganizerImpl[F[_]](
  private val organizationAlgebra: OrganizationAlgebra[F],
)(implicit F:                      Sync[F])
    extends OrganizationOrganizer[F] {

  override def getOrganizationList(pagingInfo: PagingInfo)(implicit ctx: UserAuthCtx): F[List[OrganizationList]] =
    for {
      viewableOrgIds <- ctx.organizations.map(_.id).pure[F]
      organizations  <- organizationAlgebra.getAll(Some(viewableOrgIds), pagingInfo)(ctx.userId)
    } yield organizations

  override def registerOrganization(definition: OrganizationDefinition)(
    implicit ctx:                               UserAuthCtx,
  ): F[OrganizationProfile] =
    for {
      organization <- organizationAlgebra.create(definition)(ctx.userId)
    } yield organization

  override def getOrganizationProfile(id: OrganizationID)(implicit ctx: UserAuthCtx): F[OrganizationProfile] =
    for {
      organization <- organizationAlgebra.getById(id)(ctx.userId)
    } yield organization

  override def updateOrganizationProfile(id: OrganizationID, definition: OrganizationDefinition)(
    implicit ctx:                            UserAuthCtx,
  ): F[OrganizationProfile] =
    for {
      organization <- organizationAlgebra.updateById(id, definition)(ctx.userId)
    } yield organization

  override def deleteOrganization(id: OrganizationID)(implicit ctx: UserAuthCtx): F[Unit] =
    for {
      _ <- organizationAlgebra.deleteById(id)
    } yield ()

  override def getOrganizationInvites(id: OrganizationID, pagingInfo: PagingInfo, statusFilter: Option[InviteStatus])(
    implicit ctx:                         UserAuthCtx,
  ): F[List[OrganizationInviteList]] =
    for {
      invites <- organizationAlgebra.getOrganizationInvites(id, pagingInfo, statusFilter)
    } yield invites

  override def invite(id: OrganizationID, invite: OrganizationInviteDefinition)(
    implicit ctx:         UserAuthCtx,
  ): F[OrganizationInvite] =
    for {
      invite <- organizationAlgebra.sendInvite(id, invite)
    } yield invite

  override def cancelInvite(id: OrganizationID, inviteId: OrganizationInviteID)(implicit ctx: UserAuthCtx): F[Unit] =
    for {
      _ <- organizationAlgebra.cancelInvite(id, inviteId)
    } yield ()

  override def join(code: InviteCode)(implicit ctx: UserAuthCtx): F[OrganizationProfile] =
    for {
      organization <- organizationAlgebra.join(code)(ctx.userId, ctx.email)
    } yield organization

  override def setInviteStatus(id: OrganizationID, inviteId: OrganizationInviteID, status: InviteStatus)(
    implicit ctx:                  UserAuthCtx,
  ): F[Unit] =
    for {
      organization <- organizationAlgebra.setInviteStatus(id, inviteId, status)(ctx.userId, ctx.email)
    } yield organization

  override def getOrganizationMemberList(id: OrganizationID, pagingInfo: PagingInfo)(
    implicit ctx:                            UserAuthCtx,
  ): F[List[OrganizationMemberList]] =
    for {
      members <- organizationAlgebra.getMembersList(id, pagingInfo)
    } yield members

  override def updateOrganizationMember(id: OrganizationID, userId: UserID, definition: OrganizationMemberDefinition)(
    implicit ctx:                           UserAuthCtx,
  ): F[OrganizationMember] =
    for {
      member <- organizationAlgebra.updateMemberByUserID(id, userId, definition)
    } yield member

  override def removeOrganizationMember(id: OrganizationID, userId: UserID)(implicit ctx: UserAuthCtx): F[Unit] =
    for {
      _ <- organizationAlgebra.removeMemberByUserID(id, userId)
    } yield ()
}
