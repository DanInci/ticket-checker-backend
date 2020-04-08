package ticheck.organizer.organization.impl

import ticheck.{OrganizationID, OrganizationInviteID, PagingInfo, UserID}
import ticheck.algebra.organization.OrganizationAlgebra
import ticheck.algebra.organization.models._
import ticheck.algebra.user.models.auth.UserAuthCtx
import ticheck.dao.organization.invite.InviteCode
import ticheck.organizer.organization.OrganizationOrganizer

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[organization] case class OrganizationOrganizerImpl[F[_]](
  private val organizationAlgebra: OrganizationAlgebra[F],
) extends OrganizationOrganizer[F] {
  override def getOrganizationList(pagingInfo: PagingInfo)(implicit ctx: UserAuthCtx): F[List[OrganizationList]] = ???

  override def registerOrganization(definition: OrganizationDefinition)(
    implicit ctx:                               UserAuthCtx,
  ): F[OrganizationProfile] = ???

  override def getOrganizationProfile(id: OrganizationID)(implicit ctx: UserAuthCtx): F[OrganizationProfile] = ???

  override def updateOrganizationProfile(id: OrganizationID, definition: OrganizationDefinition)(
    implicit ctx:                            UserAuthCtx,
  ): F[OrganizationProfile] = ???

  override def deleteOrganization(id: OrganizationID)(implicit ctx: UserAuthCtx): F[Unit] = ???

  override def invite(id: OrganizationID, invite: OrganizationInviteDefinition)(
    implicit ctx:         UserAuthCtx,
  ): F[OrganizationInvite] = ???

  override def cancelInvite(id: OrganizationID, inviteId: OrganizationInviteID)(implicit ctx: UserAuthCtx): F[Unit] =
    ???

  override def join(code: InviteCode)(implicit ctx: UserAuthCtx): F[OrganizationProfile] = ???

  override def getOrganizationMemberList(id: OrganizationID, pagingInfo: PagingInfo)(
    implicit ctx:                            UserAuthCtx,
  ): F[List[OrganizationMemberList]] = ???

  override def updateOrganizationMember(id: OrganizationID, userId: UserID, definition: OrganizationMemberDefinition)(
    implicit ctx:                           UserAuthCtx,
  ): F[OrganizationMember] = ???

  override def removeOrganizationMember(id: OrganizationID, userId: UserID)(implicit ctx: UserAuthCtx): F[Unit] = ???
}
