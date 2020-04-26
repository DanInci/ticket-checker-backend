package ticheck.algebra.ticket.impl

import java.time.{DayOfWeek, LocalTime}

import ticheck._
import ticheck.algebra.organization.OrganizationNFA
import ticheck.algebra.ticket.IntervalType._
import ticheck.algebra.ticket._
import ticheck.algebra.ticket.models._
import ticheck.dao.organization.OrganizationSQL
import ticheck.dao.ticket._
import ticheck.dao.ticket.models.TicketRecord
import ticheck.dao.user.UserSQL
import ticheck.db._
import ticheck.effect._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[ticket] class TicketAlgebraImpl[F[_]] private (
  timeAlgebra:     TimeAlgebra,
  userSQL:         UserSQL[ConnectionIO],
  ticketSQL:       TicketSQL[ConnectionIO],
  organizationSQL: OrganizationSQL[ConnectionIO],
)(implicit F:      Async[F], transactor: Transactor[F])
    extends TicketAlgebra[F] with TicketStatisticsAlgebra[F] with DBOperationsAlgebra[F] {

  override def getAllForOrganization(
    organizationId: OrganizationID,
    pagingInfo:     PagingInfo,
    byCategory:     Option[TicketCategory],
    byUserId:       Option[UserID],
    searchVal:      Option[String],
  ): F[List[TicketList]] = transact {
    for {
      ticketDaos <- ticketSQL.getAllForOrganization(
        organizationId,
        pagingInfo.getOffset,
        pagingInfo.getLimit,
        byCategory,
        byUserId,
        searchVal,
      )
      tickets = ticketDaos.map(TicketList.fromDAO)
    } yield tickets
  }

  override def create(
    organizationId:  OrganizationID,
    definition:      TicketDefinition,
  )(implicit userId: UserID, name: Name): F[Ticket] = transact {
    for {
      _   <- checkCreate(organizationId, definition)
      now <- timeAlgebra.now[ConnectionIO].map(SoldAt.spook)
      ticketDAO = TicketRecord(
        definition.id,
        organizationId,
        definition.soldTo,
        definition.soldToBirthday,
        definition.soldToTelephone,
        Some(SoldBy.spook(userId)),
        SoldByName.spook(name),
        now,
        None,
        None,
        None,
      )
      _      <- ticketSQL.insert(ticketDAO)
      soldBy <- ticketDAO.soldBy.traverse(id => userSQL.find(UserID.spook(id)))
      validatedBy <- ticketDAO.validatedBy.traverse(
        id => userSQL.find(UserID.spook(id)),
      )
    } yield Ticket.fromDAO(ticketDAO, soldBy.flatten, validatedBy.flatten)
  }

  /*
   * - check if organization with id exists
   * - check if ticket with the same id does not exist for organization
   */
  private def checkCreate(organizationId: OrganizationID, definition: TicketDefinition): ConnectionIO[Unit] =
    for {
      _ <- organizationSQL
        .find(organizationId)
        .flattenOption(OrganizationNFA(organizationId))
      _ <- ticketSQL
        .find((definition.id, organizationId))
        .ifSomeRaise(TicketAlreadyExistsCA(organizationId, definition.id))
    } yield ()

  override def getById(organizationId: OrganizationID, ticketId: TicketID): F[Ticket] = transact {
    for {
      ticketDAO <- ticketSQL
        .find((ticketId, organizationId))
        .flattenOption(TicketNFA(organizationId, ticketId))
      soldBy <- ticketDAO.soldBy.traverse(id => userSQL.find(UserID.spook(id)))
      validatedBy <- ticketDAO.validatedBy.traverse(
        id => userSQL.find(UserID.spook(id)),
      )
    } yield Ticket.fromDAO(ticketDAO, soldBy.flatten, validatedBy.flatten)
  }

  override def updateById(
    organizationId: OrganizationID,
    ticketId:       TicketID,
    definition:     TicketUpdateDefinition,
  ): F[Ticket] = transact {
    for {
      ticketDAO <- checkUpdate(
        organizationId: OrganizationID,
        ticketId:       TicketID,
        definition:     TicketUpdateDefinition,
      )
      updatedTicketDAO = ticketDAO.copy(
        soldTo          = definition.soldTo,
        soldToBirthday  = definition.soldToBirthday,
        soldToTelephone = definition.soldToTelephone,
      )
      _ <- ticketSQL.update(updatedTicketDAO)
      soldBy <- updatedTicketDAO.soldBy.traverse(
        id => userSQL.find(UserID.spook(id)),
      )
      validatedBy <- updatedTicketDAO.validatedBy
        .traverse(id => userSQL.find(UserID.spook(id)))
    } yield Ticket.fromDAO(updatedTicketDAO, soldBy.flatten, validatedBy.flatten)
  }

  /*
   * - check if organization with id exists
   * - check if ticket with id exists for organization
   */
  private def checkUpdate(
    organizationId: OrganizationID,
    ticketId:       TicketID,
    definition:     TicketUpdateDefinition,
  ): ConnectionIO[TicketRecord] =
    for {
      _ <- organizationSQL
        .find(organizationId)
        .flattenOption(OrganizationNFA(organizationId))
      ticketDAO <- ticketSQL
        .find((ticketId, organizationId))
        .flattenOption(TicketNFA(organizationId, ticketId))
      _ <- definition.pure[ConnectionIO]
    } yield ticketDAO

  override def setValidationStatusById(organizationId: OrganizationID, ticketId: TicketID, isValidated: IsValidated)(
    implicit userId:                                   UserID,
    name:                                              Name,
  ): F[Ticket] = transact {
    for {
      ticketDAO <- checkUpdateValidatedStatus(
        organizationId,
        ticketId,
        isValidated,
      )
      updatedTicketDAO <- if (isValidated) {
        timeAlgebra
          .now[ConnectionIO]
          .map(
            now =>
              ticketDAO.copy(
                validatedBy     = Some(ValidatedBy.spook(userId)),
                validatedByName = Some(ValidatedByName.spook(name)),
                validatedAt     = Some(ValidatedAt.spook(now)),
              ),
          )
      }
      else {
        ticketDAO
          .copy(validatedBy = None, validatedByName = None, validatedAt = None)
          .pure[ConnectionIO]
      }
      _ <- ticketSQL.update(updatedTicketDAO)
      soldBy <- updatedTicketDAO.soldBy.traverse(
        id => userSQL.find(UserID.spook(id)),
      )
      validatedBy <- updatedTicketDAO.validatedBy
        .traverse(id => userSQL.find(UserID.spook(id)))
    } yield Ticket.fromDAO(updatedTicketDAO, soldBy.flatten, validatedBy.flatten)
  }

  /*
   * - check if organization with id exists
   * - check if ticket with id exists for organization
   * - check if ticket is already validated/not validated
   */
  private def checkUpdateValidatedStatus(
    organizationId: OrganizationID,
    ticketId:       TicketID,
    isValidated:    IsValidated,
  ): ConnectionIO[TicketRecord] =
    for {
      _ <- organizationSQL
        .find(organizationId)
        .flattenOption(OrganizationNFA(organizationId))
      ticketDAO <- ticketSQL
        .find((ticketId, organizationId))
        .flattenOption(TicketNFA(organizationId, ticketId))
      _ <- (isValidated && ticketDAO.validatedAt.isDefined)
        .ifTrueRaise[ConnectionIO](
          TicketAlreadyValidatedCA(organizationId, ticketId),
        )
      _ <- (!isValidated && ticketDAO.validatedAt.isEmpty)
        .ifTrueRaise[ConnectionIO](
          TicketAlreadyNotValidatedCA(organizationId, ticketId),
        )
    } yield ticketDAO

  override def deleteById(organizationId: OrganizationID, ticketId: TicketID): F[Unit] = transact {
    for {
      _ <- checkDelete(organizationId, ticketId)
      _ <- ticketSQL.delete((ticketId, organizationId))
    } yield ()
  }

  /*
   * - check if organization with id exists
   * - check if ticket with id exists for organization
   */
  private def checkDelete(organizationId: OrganizationID, ticketId: TicketID): ConnectionIO[Unit] =
    for {
      _ <- organizationSQL
        .find(organizationId)
        .flattenOption(OrganizationNFA(organizationId))
      _ <- ticketSQL
        .find((ticketId, organizationId))
        .flattenOption(TicketNFA(organizationId, ticketId))
    } yield ()

  override def getTicketsCount(
    organizationId: OrganizationID,
    byCategory:     Option[TicketCategory],
    searchValue:    Option[String],
  ): F[Count] = transact {
    ticketSQL.countBy(organizationId, byCategory, searchValue)
  }

  override def getCountStats(
    organizationId: OrganizationID,
    byCategory:     TicketCategory,
    byInterval:     IntervalType,
    howMany:        StatisticsSize,
    until:          StatisticsTimestamp,
  ): F[List[TicketStatistic]] = transact {
    for {
      finalDateTime <- timeAlgebra.toOffsetDateTime(until).pure[ConnectionIO]
      calibratedDateTime = byInterval match {
        case HourlyInterval =>
          finalDateTime.`with`(LocalTime.of(finalDateTime.getHour, 59, 59))
        case DailyInterval => finalDateTime.`with`(LocalTime.MAX)
        case WeeklyInterval =>
          finalDateTime.`with`(LocalTime.MAX).`with`(DayOfWeek.SUNDAY)
      }
      statistics: List[TicketStatistic] <- List
        .range(howMany - 1, -1, -1)
        .traverse(no => {
          for {
            (startDate, endDate) <- byInterval match {
              case HourlyInterval =>
                val startDate =
                  calibratedDateTime.minusHours((no + 1).toLong).plusSeconds(1)
                val endDate = calibratedDateTime.minusHours(no.toLong)
                (StartDate.spook(startDate), EndDate.spook(endDate))
                  .pure[ConnectionIO]
              case DailyInterval =>
                val startDate =
                  calibratedDateTime.minusDays((no + 1).toLong).plusSeconds(1)
                val endDate = calibratedDateTime.minusDays(no.toLong)
                (StartDate.spook(startDate), EndDate.spook(endDate))
                  .pure[ConnectionIO]
              case WeeklyInterval =>
                val startDate =
                  calibratedDateTime.minusWeeks((no + 1).toLong).plusSeconds(1)
                val endDate = calibratedDateTime.minusWeeks(no.toLong)
                (StartDate.spook(startDate), EndDate.spook(endDate))
                  .pure[ConnectionIO]
            }
            count <- ticketSQL.countTicketsBetweenDates(
              organizationId,
              byCategory,
              startDate,
              endDate,
            )
          } yield TicketStatistic(count, startDate, endDate),
        })
    } yield statistics
  }

}

private[ticket] object TicketAlgebraImpl {

  def async[F[_]: Async: Transactor](
    timeAlgebra:     TimeAlgebra,
    userSQL:         UserSQL[ConnectionIO],
    ticketSQL:       TicketSQL[ConnectionIO],
    organizationSQL: OrganizationSQL[ConnectionIO],
  ): F[TicketModuleAlgebra[F]] =
    Async[F].pure(
      new TicketAlgebraImpl[F](timeAlgebra, userSQL, ticketSQL, organizationSQL),
    )

}
