package ticheck.algebra.user.impl

import ticheck._
import ticheck.algebra.user.models._
import ticheck.algebra.user._
import ticheck.dao.organization.membership.{OrganizationMembershipSQL, OrganizationRole}
import ticheck.dao.ticket.{SoldByName, TicketCategory, TicketSQL, ValidatedByName}
import ticheck.dao.user.{EditedAt, UserSQL}
import ticheck.dao.user.models.UserRecord
import ticheck.effect._
import ticheck.db._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[user] class UserAlgebraImpl[F[_]] private (
  timeAlgebra:               TimeAlgebra,
  userSQL:                   UserSQL[ConnectionIO],
  ticketSQL:                 TicketSQL[ConnectionIO],
  organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
)(implicit F:                Async[F], transactor: Transactor[F])
    extends UserAlgebra[F] with DBOperationsAlgebra[F] {

  override def getProfileById(id: UserID): F[UserProfile] = transact {
    for {
      userDAO <- userSQL.find(id).flattenOption(new UserNFA(id))
    } yield UserProfile.fromDAO(userDAO)
  }

  override def getProfileByEmail(email: Email): F[UserProfile] = transact {
    for {
      userDAO <- userSQL.findByEmail(email).flattenOption(new UserNFA(email))
    } yield UserProfile.fromDAO(userDAO)
  }

  override def updateById(id: UserID, definition: UserDefinition): F[UserProfile] = transact {
    for {
      currentUserDAO <- checkUserUpdate(id, definition)
      now            <- timeAlgebra.now[ConnectionIO].map(EditedAt.spook)
      newUserDAO <- if (currentUserDAO.name != definition.name) {
        for {
          newUserDAO <- currentUserDAO.copy(name = definition.name, editedAt = Some(now)).pure[ConnectionIO]
          _          <- userSQL.update(newUserDAO)

          // update tickets name
          soldTickets      <- ticketSQL.findByUserID(id, category = Some(TicketCategory.SoldTicket))
          _                <- ticketSQL.updateMany(soldTickets.map(_.copy(soldByName = SoldByName.spook(newUserDAO.name))))
          validatedTickets <- ticketSQL.findByUserID(id, category = Some(TicketCategory.ValidatedTicket))
          _ <- ticketSQL.updateMany(
            validatedTickets.map(_.copy(validatedByName = Some(ValidatedByName.spook(newUserDAO.name)))),
          )
        } yield newUserDAO
      }
      else currentUserDAO.pure[ConnectionIO]
    } yield UserProfile.fromDAO(newUserDAO)
  }

  override def deleteById(id: UserID): F[Unit] = transact {
    for {
      _ <- checkUserDelete(id)
      _ <- userSQL.delete(id)
    } yield ()
  }

  /*
   * check if user with id exists
   */
  private def checkUserUpdate(id: UserID, definition: UserDefinition): ConnectionIO[UserRecord] =
    for {
      userDAO <- userSQL.find(id).flattenOption(new UserNFA(id))
      _       <- definition.pure[ConnectionIO]
    } yield userDAO

  /*
   * - check if user with id exists
   * - check if user is not an organization owner
   */
  private def checkUserDelete(id: UserID): ConnectionIO[Unit] =
    for {
      _              <- userSQL.find(id).flattenOption(new UserNFA(id))
      membershipDAOs <- organizationMembershipSQL.getByUserID(id)
      _ <- membershipDAOs
        .exists(_.role == OrganizationRole.OrganizationOwner)
        .pure[ConnectionIO]
        .ifTrueRaise(UserIsOrganizationOwnerCA(id))
    } yield ()

}

private[user] object UserAlgebraImpl {

  def async[F[_]: Async: Transactor](
    timeAlgebra:               TimeAlgebra,
    userSQL:                   UserSQL[ConnectionIO],
    ticketSQL:                 TicketSQL[ConnectionIO],
    organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
  ): F[UserModuleAlgebra[F]] =
    Async[F].pure(new UserAlgebraImpl[F](timeAlgebra, userSQL, ticketSQL, organizationMembershipSQL))

}
