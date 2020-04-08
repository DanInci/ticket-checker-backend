package ticheck.algebra.organization.impl

import ticheck.{OrganizationID, OrganizationInviteID, PagingInfo, UserID}
import ticheck.algebra.organization.models.{
  OrganizationDefinition,
  OrganizationInvite,
  OrganizationInviteDefinition,
  OrganizationList,
  OrganizationMember,
  OrganizationMemberDefinition,
  OrganizationMemberList,
  OrganizationProfile,
}
import ticheck.algebra.organization.{OrganizationAlgebra, OrganizationModuleAlgebra}
import ticheck.dao.organization.OrganizationSQL
import ticheck.dao.organization.invite.{InviteCode, OrganizationInviteSQL}
import ticheck.dao.organization.membership.OrganizationMembershipSQL
import ticheck.db._
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[organization] class OrganizationAlgebraImpl[F[_]] private (
  organizationSQL:           OrganizationSQL[ConnectionIO],
  organizationInviteSQL:     OrganizationInviteSQL[ConnectionIO],
  organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
)(implicit F:                Async[F], transactor: Transactor[F])
    extends OrganizationAlgebra[F] with DBOperationsAlgebra[F] {

  override def getAll(filter: Option[List[OrganizationID]], pagingInfo: PagingInfo): F[List[OrganizationList]] = ???

  override def create(definition: OrganizationDefinition): F[OrganizationProfile] = ???

  override def getById(id: OrganizationID): F[OrganizationProfile] = ???

  override def updateById(id: OrganizationID, definition: OrganizationDefinition): F[OrganizationProfile] = ???

  override def deleteById(id: OrganizationID): F[Unit] = ???

  override def sendInvite(id: OrganizationID, invite: OrganizationInviteDefinition): F[OrganizationInvite] = ???

  override def cancelInvite(id: OrganizationID, inviteId: OrganizationInviteID): F[Unit] = ???

  override def join(inviteCode: InviteCode): F[OrganizationProfile] = ???

  override def getMembersList(id: OrganizationID, pagingInfo: PagingInfo): F[List[OrganizationMemberList]] = ???

  override def updateMemberByUserID(
    id:         OrganizationID,
    userId:     UserID,
    definition: OrganizationMemberDefinition,
  ): F[OrganizationMember] = ???

  override def removeMemberByUserID(id: OrganizationID, userId: UserID): F[Unit] = ???

}

private[organization] object OrganizationAlgebraImpl {

  def async[F[_]: Async: Transactor](
    organizationSQL:           OrganizationSQL[ConnectionIO],
    organizationInviteSQL:     OrganizationInviteSQL[ConnectionIO],
    organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
  ): F[OrganizationModuleAlgebra[F]] =
    Async[F].pure(
      new OrganizationAlgebraImpl[F](organizationSQL, organizationInviteSQL, organizationMembershipSQL),
    )

}
