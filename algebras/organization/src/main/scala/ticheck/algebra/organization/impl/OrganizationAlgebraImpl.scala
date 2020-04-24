package ticheck.algebra.organization.impl

import ticheck._
import ticheck.algebra.organization.models._
import ticheck.algebra.organization._
import ticheck.dao.organization.invite.InviteStatus.{InviteAccepted, InviteDeclined, InvitePending}
import ticheck.dao.organization.{OrganizationSQL, OwnerID}
import ticheck.dao.organization.invite._
import ticheck.dao.organization.invite.models.OrganizationInviteRecord
import ticheck.dao.organization.membership.{JoinedAt, OrganizationMembershipSQL, OrganizationRole}
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.dao.organization.models.OrganizationRecord
import ticheck.dao.user.UserSQL
import ticheck.db._
import ticheck.effect._
import ticheck.email.{EmailAlgebra, EmailMessage, EmailTitle}
import ticheck.logger._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[organization] class OrganizationAlgebraImpl[F[_]] private (
  timeAlgebra:               TimeAlgebra,
  emailAlgebra:              EmailAlgebra[F],
  userSQL:                   UserSQL[ConnectionIO],
  organizationSQL:           OrganizationSQL[ConnectionIO],
  organizationInviteSQL:     OrganizationInviteSQL[ConnectionIO],
  organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
)(implicit F:                Async[F], transactor: Transactor[F], timer: Timer[F])
    extends OrganizationAlgebra[F] with OrganizationStatisticsAlgebra[F] with DBOperationsAlgebra[F] {

  implicit private val loggerF: Logger[F] = Logger.getLogger[F]

  override def getAll(filter: Option[List[OrganizationID]], pagingInfo: PagingInfo)(
    implicit userId:          UserID,
  ): F[List[OrganizationList]] =
    transact {
      for {
        organizationDAOs <- organizationSQL.getAll(filter, pagingInfo.getOffset, pagingInfo.getLimit)
        organizations <- organizationDAOs.traverse { o =>
          for {
            om <- organizationMembershipSQL.findForOrganizationByUserID(o.id, userId)
          } yield OrganizationList.fromDAO(o, om)
        }
      } yield organizations
    }

  override def create(definition: OrganizationDefinition)(implicit createdBy: UserID): F[OrganizationProfile] =
    transact {
      for {
        _     <- checkCreate(definition)
        orgId <- OrganizationID.generate[ConnectionIO]
        now   <- timeAlgebra.now[ConnectionIO]

        // create organization
        orgDAO = OrganizationRecord(orgId, OwnerID.spook(createdBy), definition.name, CreatedAt.spook(now))
        _ <- organizationSQL.insert(orgDAO)

        // create owner membership for organization
        orgMemId <- OrganizationMembershipID.generate[ConnectionIO]
        orgMembershipDAO = OrganizationMembershipRecord(
          orgMemId,
          createdBy,
          orgId,
          None,
          OrganizationRole.OrganizationOwner,
          JoinedAt.spook(now),
        )
        _ <- organizationMembershipSQL.insert(orgMembershipDAO)
      } yield OrganizationProfile.fromDAO(orgDAO, Some(orgMembershipDAO))
    }

  override def getById(id: OrganizationID)(implicit userId: UserID): F[OrganizationProfile] = transact {
    for {
      orgDAO    <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      orgMemDAO <- organizationMembershipSQL.findForOrganizationByUserID(id, userId)
    } yield OrganizationProfile.fromDAO(orgDAO, orgMemDAO)
  }

  override def updateById(id: OrganizationID, definition: OrganizationDefinition)(
    implicit userId:          UserID,
  ): F[OrganizationProfile] = transact {
    for {
      currentDAO <- checkUpdate(id, definition)
      newDAO = currentDAO.copy(name = definition.name)
      _         <- organizationSQL.update(newDAO)
      orgMemDAO <- organizationMembershipSQL.findForOrganizationByUserID(id, userId)
    } yield OrganizationProfile.fromDAO(newDAO, orgMemDAO)
  }

  override def deleteById(id: OrganizationID): F[Unit] = transact {
    for {
      _ <- checkDelete(id)
      _ <- organizationSQL.delete(id)
    } yield ()
  }

  override def getUserInvites(
    userId:       UserID,
    pagingInfo:   PagingInfo,
    statusFilter: Option[InviteStatus],
  ): F[List[OrganizationInviteList]] = transact {
    for {
      inviteDaos <- organizationInviteSQL.getAllForUser(userId, pagingInfo.getOffset, pagingInfo.getLimit, statusFilter)
      invites <- inviteDaos.traverse { invDAO =>
        for {
          orgDAO <- organizationSQL.retrieve(invDAO.organizationId)
        } yield OrganizationInviteList.fromDAO(invDAO, orgDAO)
      }
    } yield invites
  }

  override def getOrganizationInvites(
    organizationId: OrganizationID,
    pagingInfo:     PagingInfo,
    statusFilter:   Option[InviteStatus],
  ): F[List[OrganizationInviteList]] = transact {
    for {
      inviteDaos <- organizationInviteSQL
        .getAllForOrganization(organizationId, pagingInfo.getOffset, pagingInfo.getLimit, statusFilter)
      invites <- inviteDaos.traverse { invDAO =>
        for {
          orgDAO <- organizationSQL.retrieve(invDAO.organizationId)
        } yield OrganizationInviteList.fromDAO(invDAO, orgDAO)
      }
    } yield invites
  }

  override def sendInvite(id: OrganizationID, definition: OrganizationInviteDefinition): F[OrganizationInvite] = {
    import scala.concurrent.duration._
    for {
      (organization, invitation, inviteCode) <- transact {
        for {
          orgDAO     <- checkSendInvite(id, definition)
          inviteId   <- OrganizationInviteID.generate[ConnectionIO]
          random     <- SecureRandom.newSecureRandom[ConnectionIO]
          inviteCode <- generateInviteCode[ConnectionIO](implicitly, random)
          now        <- timeAlgebra.now[ConnectionIO].map(InvitedAt.spook)
          inviteDAO = OrganizationInviteRecord(
            inviteId,
            id,
            definition.email,
            inviteCode,
            InviteStatus.InvitePending,
            None,
            now,
          )
          _ <- organizationInviteSQL.insert(inviteDAO)
        } yield (orgDAO, OrganizationInvite.fromDAO(inviteDAO, orgDAO), inviteCode)
      }
      emailTitle    = EmailTitle.spook("Invitation into organization")
      confirmIntent = s"https://ticheck.elementum.ro/join-organization/$inviteCode"
      emailMessage = EmailMessage.spook(
        s"ou have been invited to join ${organization.name}!\n\nYour invite code is: $inviteCode\n\nYou can also join the organization by clicking $confirmIntent",
      )
      _ <- emailAlgebra
        .sendEmail(
          invitation.email,
          emailTitle,
          emailMessage,
        )
        .reattempt(
          (e, s) => loggerF.warn(e)(s"Failed to send 'Invitation into organization' Email to '${invitation.email}'. $s"),
        )(
          retries = 1,
          1.second,
        )
    } yield invitation
  }

  override def cancelInvite(id: OrganizationID, inviteId: OrganizationInviteID): F[Unit] = transact {
    for {
      _ <- checkCancelInvite(id, inviteId)
      _ <- organizationInviteSQL.delete(inviteId)
    } yield ()
  }

  override def join(inviteCode: InviteCode)(implicit userId: UserID, email: Email): F[OrganizationProfile] = transact {
    for {
      inviteDAO <- checkJoin(inviteCode)
      orgDAO    <- invitationAccepted(inviteDAO)
      orgMemDAO <- organizationMembershipSQL.findForOrganizationByUserID(orgDAO.id, userId)
    } yield OrganizationProfile.fromDAO(orgDAO, orgMemDAO)
  }

  override def setInviteStatus(
    id:              OrganizationID,
    inviteId:        OrganizationInviteID,
    status:          InviteStatus,
  )(implicit userId: UserID, email: Email): F[OrganizationProfile] =
    transact {
      for {
        (orgDAO, inviteDAO) <- checkSetInviteStatus(id, inviteId)
        _ <- status match {
          case InviteAccepted => invitationAccepted(inviteDAO).void
          case InviteDeclined => invitationDeclined(inviteDAO)
          case _              => ConnectionIO.unit
        }
        membership <- organizationMembershipSQL.findForOrganizationByUserID(id, userId)
      } yield OrganizationProfile.fromDAO(orgDAO, membership)
    }

  override def getMembersList(
    id:           OrganizationID,
    pagingInfo:   PagingInfo,
    byRole:       Option[OrganizationRole],
    searchFilter: Option[String],
  ): F[List[OrganizationMemberList]] = transact {
    for {
      membersDaos <- organizationMembershipSQL.getAllForOrganization(
        id,
        pagingInfo.getOffset,
        pagingInfo.getLimit,
        byRole:       Option[OrganizationRole],
        searchFilter: Option[String],
      )
      memberUserDaos <- membersDaos.traverse(m => userSQL.retrieve(m.userId).map(u => (m, u)))
      members = memberUserDaos.map(mu => OrganizationMemberList.fromDAO(mu._1, mu._2))
    } yield members
  }

  override def getOrganizationMemberById(id: OrganizationID, userId: UserID): F[OrganizationMember] = transact {
    for {
      orgDAO <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      orgMemDAO <- organizationMembershipSQL
        .findForOrganizationByUserID(id, userId)
        .flattenOption(OrganizationMemberNFA(id, userId))
      userDAO            <- userSQL.retrieve(userId)
      soldTicketsNo      <- organizationMembershipSQL.getSoldTicketsCountFor(id, userId)
      validatedTicketsNo <- organizationMembershipSQL.getValidatedTicketsCountFor(id, userId)
    } yield OrganizationMember.fromDAO(orgDAO, userDAO, orgMemDAO, soldTicketsNo, validatedTicketsNo)
  }

  override def updateMemberByUserID(
    id:         OrganizationID,
    userId:     UserID,
    definition: OrganizationMemberDefinition,
  ): F[OrganizationMember] = transact {
    for {
      (orgDAO, memberDAO) <- checkUpdateMember(id, userId, definition)
      updatedMemberDAO = memberDAO.copy(role = definition.role)
      _                  <- organizationMembershipSQL.update(updatedMemberDAO)
      userDAO            <- userSQL.retrieve(memberDAO.userId)
      soldTicketsNo      <- organizationMembershipSQL.getSoldTicketsCountFor(id, userId)
      validatedTicketsNo <- organizationMembershipSQL.getValidatedTicketsCountFor(id, userId)
    } yield OrganizationMember.fromDAO(orgDAO, userDAO, updatedMemberDAO, soldTicketsNo, validatedTicketsNo)
  }

  override def removeMemberByUserID(id: OrganizationID, userId: UserID): F[Unit] = transact {
    for {
      memberDAO <- checkDeleteMember(id: OrganizationID, userId: UserID)
      _         <- organizationMembershipSQL.delete(memberDAO.id)
    } yield ()
  }

  override def getOrganizationMembersCount(
    organizationId: OrganizationID,
    byRole:         Option[OrganizationRole],
    searchValue:    Option[String],
  ): F[Count] = transact {
    organizationMembershipSQL.countBy(organizationId, byRole, searchValue)
  }

  private def generateInviteCode[H[_]: Sync: SecureRandom]: H[InviteCode] = {
    val InviteCodeLength   = 6
    val InviteCodeAlphabet = "ABCDEFGHI123456789"
    SecureRandom[H].randomString(InviteCodeAlphabet)(InviteCodeLength).map(InviteCode.spook)
  }

  private def invitationAccepted(
    inviteDAO:       OrganizationInviteRecord,
  )(implicit userId: UserID): ConnectionIO[OrganizationRecord] =
    for {
      now <- timeAlgebra.now[ConnectionIO]
      id  <- OrganizationMembershipID.generate[ConnectionIO]
      membershipDAO = OrganizationMembershipRecord(
        id,
        userId,
        inviteDAO.organizationId,
        Some(inviteDAO.id),
        OrganizationRole.User,
        JoinedAt.spook(now),
      )
      updatedInviteDAO = inviteDAO.copy(status = InviteStatus.InviteAccepted, answeredAt = Some(AnsweredAt.spook(now)))
      _      <- organizationMembershipSQL.insert(membershipDAO)
      _      <- organizationInviteSQL.update(updatedInviteDAO)
      orgDAO <- organizationSQL.retrieve(updatedInviteDAO.organizationId)
    } yield orgDAO

  private def invitationDeclined(inviteDAO: OrganizationInviteRecord): ConnectionIO[Unit] =
    for {
      now <- timeAlgebra.now[ConnectionIO]
      updatedInviteDAO = inviteDAO.copy(status = InviteStatus.InviteDeclined, answeredAt = Some(AnsweredAt.spook(now)))
      _ <- organizationInviteSQL.update(updatedInviteDAO)
    } yield ()

  /*
   * - check if organization with the same name does not exist
   */
  private def checkCreate(definition: OrganizationDefinition): ConnectionIO[Unit] =
    for {
      _ <- organizationSQL.findByName(definition.name).ifSomeRaise(OrganizationAlreadyExistsCA(definition.name))
    } yield ()

  /*
   * - check if organization with id exists
   * - check if organization with the same name does not exist (only if name is different)
   */
  private def checkUpdate(id: OrganizationID, definition: OrganizationDefinition): ConnectionIO[OrganizationRecord] =
    for {
      orgDAO <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      _ <- (orgDAO.name != definition.name).ifTrueRun {
        organizationSQL.findByName(definition.name).ifSomeRaise(OrganizationAlreadyExistsCA(definition.name))
      }
    } yield orgDAO

  /*
   * - check if organization with id exists
   */
  private def checkDelete(id: OrganizationID): ConnectionIO[OrganizationRecord] =
    for {
      orgDAO <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
    } yield orgDAO

  /*
   * - check if organization with id exists
   * - check if invite for email exists and is still pending
   * - check if user with email is not already a member of the organization
   */
  private def checkSendInvite(
    id:         OrganizationID,
    definition: OrganizationInviteDefinition,
  ): ConnectionIO[OrganizationRecord] =
    for {
      orgDAO <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      _ <- organizationInviteSQL
        .findForOrganizationByEmail(id, definition.email)
        .map(_.find(_.status == InvitePending))
        .ifSomeRaise(OrganizationInviteForEmailExistsCA(id, definition.email))
      _ <- organizationMembershipSQL
        .findForOrganizationByEmail(id, definition.email)
        .ifSomeRaise(OrganizationMembershipForEmailExistsCA(id, definition.email))
    } yield orgDAO

  /*
   * - check if organization with id exists
   * - check if invite with id exists for organization !!!
   * - check if invite is not answered
   */
  private def checkCancelInvite(id: OrganizationID, inviteId: OrganizationInviteID): ConnectionIO[Unit] =
    for {
      _ <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      inviteDAO <- organizationInviteSQL
        .find(inviteId)
        .map(_.filter(_.organizationId == id))
        .flattenOption(new OrganizationInviteNFA(inviteId))
      _ <- (inviteDAO.status != InviteStatus.InvitePending)
        .ifTrueRaise[ConnectionIO](new OrganizationInviteIsAnsweredCA(inviteId, inviteDAO.status))
    } yield ()

  /*
   * - check if invite with code exists
   * - check if invite is destined for the email
   * - check if invitation is not answered
   * - check if user with email is not already a member of the organization
   */
  private def checkJoin(inviteCode: InviteCode)(implicit email: Email): ConnectionIO[OrganizationInviteRecord] =
    for {
      inviteDAO <- organizationInviteSQL
        .findByInvitationCode(inviteCode)
        .map(_.filter(inv => inv.email == email))
        .flattenOption(new OrganizationInviteNFA(inviteCode))
      _ <- (inviteDAO.status != InviteStatus.InvitePending)
        .ifTrueRaise[ConnectionIO](new OrganizationInviteIsAnsweredCA(inviteCode, inviteDAO.status))
      _ <- organizationMembershipSQL
        .findForOrganizationByEmail(inviteDAO.organizationId, inviteDAO.email)
        .ifSomeRaise(OrganizationMembershipForEmailExistsCA(inviteDAO.organizationId, inviteDAO.email))
    } yield inviteDAO

  /*
   * - check if organization with id exists
   * - check if invite with id exists for organization
   * - check if invite is destined for the email
   * - check if invitation is not answered
   * - check if user with email is not already a member of the organization
   */
  private def checkSetInviteStatus(id: OrganizationID, inviteId: OrganizationInviteID)(
    implicit email:                    Email,
  ): ConnectionIO[(OrganizationRecord, OrganizationInviteRecord)] =
    for {
      orgDAO <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      inviteDAO <- organizationInviteSQL
        .find(inviteId)
        .map(_.filter(inv => inv.organizationId == id && inv.email == email))
        .flattenOption(new OrganizationInviteNFA(inviteId))
      _ <- (inviteDAO.status != InviteStatus.InvitePending)
        .ifTrueRaise[ConnectionIO](new OrganizationInviteIsAnsweredCA(inviteId, inviteDAO.status))
      _ <- organizationMembershipSQL
        .findForOrganizationByEmail(inviteDAO.organizationId, inviteDAO.email)
        .ifSomeRaise(OrganizationMembershipForEmailExistsCA(inviteDAO.organizationId, inviteDAO.email))
    } yield (orgDAO, inviteDAO)

  /*
   * - check if organization exists
   * - check if user is member of the organization
   * - check if user is not the owner of the organization
   * - check if new role is not set to OrganizationOwner
   */
  private def checkUpdateMember(
    id:         OrganizationID,
    userId:     UserID,
    definition: OrganizationMemberDefinition,
  ): ConnectionIO[(OrganizationRecord, OrganizationMembershipRecord)] =
    for {
      orgDAO <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      memberDAO <- organizationMembershipSQL
        .findForOrganizationByUserID(id, userId)
        .flattenOption(OrganizationMemberNFA(id, userId))
      _ <- (memberDAO.role == OrganizationRole.OrganizationOwner)
        .ifTrueRaise[ConnectionIO](OrganizationMemberChangesNotAllowedIIA(id, userId))
      _ <- (definition.role == OrganizationRole.OrganizationOwner)
        .ifTrueRaise[ConnectionIO](OrganizationMemberRoleNotAllowedIIA(id, userId, definition.role))
    } yield (orgDAO, memberDAO)

  /*
   * - check if organization exists
   * - check if user is member of the organization
   * * - check if user is not the owner of the organization
   */
  private def checkDeleteMember(id: OrganizationID, userId: UserID): ConnectionIO[OrganizationMembershipRecord] =
    for {
      _ <- organizationSQL.find(id).flattenOption(OrganizationNFA(id))
      memberDAO <- organizationMembershipSQL
        .findForOrganizationByUserID(id, userId)
        .flattenOption(OrganizationMemberNFA(id, userId))
      _ <- (memberDAO.role == OrganizationRole.OrganizationOwner)
        .ifTrueRaise[ConnectionIO](OrganizationMemberChangesNotAllowedIIA(id, userId))
    } yield memberDAO

}

private[organization] object OrganizationAlgebraImpl {

  def async[F[_]: Async: Transactor: Timer](
    timeAlgebra:               TimeAlgebra,
    emailAlgebra:              EmailAlgebra[F],
    userSQL:                   UserSQL[ConnectionIO],
    organizationSQL:           OrganizationSQL[ConnectionIO],
    organizationInviteSQL:     OrganizationInviteSQL[ConnectionIO],
    organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
  ): F[OrganizationModuleAlgebra[F]] =
    Async[F].pure(
      new OrganizationAlgebraImpl[F](
        timeAlgebra,
        emailAlgebra,
        userSQL,
        organizationSQL,
        organizationInviteSQL,
        organizationMembershipSQL,
      ),
    )

}
