package ticheck.rest.routes

import java.time.LocalDateTime

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}
import org.http4s.dsl.Http4sDsl
import ticheck.OrganizationID
import ticheck.algebra.ticket.{StatisticsSize, StatisticsTimestamp, _}
import ticheck.dao.organization.membership.OrganizationRole
import ticheck.dao.ticket.TicketCategory
import ticheck.http.{QueryParamInstances, RoutesHelpers}
import ticheck.effect._
import ticheck.organizer.statistic.StatisticOrganizer
import ticheck.rest._
import ticheck.http._
import ticheck.json._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class StatisticRoutes[F[_]](private val statisticOrganizer: StatisticOrganizer[F])(
  implicit val F:                                                                    Async[F],
) extends Http4sDsl[F] with RoutesHelpers with QueryParamInstances {

  implicit val statisticsSizeParamMatcher: QueryParamDecoder[StatisticsSize] =
    phantomTypeQueryParamDecoder[F, Int, StatisticsSize.Tag]

  implicit val statisticsTimestampParamMatcher: QueryParamDecoder[StatisticsTimestamp] =
    phantomTypeQueryParamDecoder[F, LocalDateTime, StatisticsTimestamp.Tag]

  implicit val organizationRoleQueryParamDecoder: QueryParamDecoder[OrganizationRole] =
    (value: QueryParameterValue) =>
      OrganizationRole
        .fromString(value.value)
        .leftMap(t => ParseFailure("Query param decoding failed", t.getMessage))
        .toValidatedNel

  implicit val ticketCategoryQueryParamDecoder: QueryParamDecoder[TicketCategory] =
    (value: QueryParameterValue) =>
      TicketCategory
        .fromString(value.value)
        .leftMap(t => ParseFailure("Query param decoding failed", t.getMessage))
        .toValidatedNel

  implicit val intervalTypeQueryParamDecoder: QueryParamDecoder[IntervalType] =
    (value: QueryParameterValue) =>
      IntervalType
        .fromString(value.value)
        .leftMap(t => ParseFailure("Query param decoding failed", t.getMessage))
        .toValidatedNel

  object TicketCategoryQueryParamMatcher    extends QueryParamDecoderMatcher[TicketCategory]("category")
  object StatisticIntervalQueryParamMatcher extends QueryParamDecoderMatcher[IntervalType]("interval")
  object StatisticsSizeQueryParamMatcher    extends OptionalQueryParamDecoderMatcher[StatisticsSize]("size")
  object StatisticsUntilQueryParamMatcher   extends OptionalQueryParamDecoderMatcher[StatisticsTimestamp]("until")
  object OptTicketCategoryQueryParamMatcher extends OptionalQueryParamDecoderMatcher[TicketCategory]("category")
  object OrganizationRoleQueryParamMatcher  extends OptionalQueryParamDecoderMatcher[OrganizationRole]("role")
  object SearchQueryParamMatcher            extends OptionalQueryParamDecoderMatcher[String]("search")

  private val organizationStatistics: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `statistics-route` / `organizations-route` / FUUIDVar(orgId) / `users-route`
          :? OrganizationRoleQueryParamMatcher(byRole) +& SearchQueryParamMatcher(searchFilter) as user =>
      for {
        count <- statisticOrganizer.getOrganizationMembersCount(OrganizationID.spook(orgId), byRole, searchFilter)(user)
        resp  <- Ok(count)
      } yield resp
  }

  private val ticketStatistics: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `statistics-route` / `organizations-route` / FUUIDVar(orgId) / `tickets-route`
          :? TicketCategoryQueryParamMatcher(category) +& StatisticIntervalQueryParamMatcher(interval)
            +& StatisticsSizeQueryParamMatcher(statsSize) +& StatisticsUntilQueryParamMatcher(statsUntil) as user =>
      for {
        statistics <- statisticOrganizer
          .getStatisticsForTickets(OrganizationID.spook(orgId), category, interval, statsSize, statsUntil)(user)
        resp <- Ok(statistics)
      } yield resp

    case GET -> Root / `statistics-route` / `organizations-route` / FUUIDVar(orgId) / `tickets-route`
          :? OptTicketCategoryQueryParamMatcher(byCategory) +& SearchQueryParamMatcher(searchFilter) as user =>
      for {
        count <- statisticOrganizer.getTicketsCount(OrganizationID.spook(orgId), byCategory, searchFilter)(user)
        resp  <- Ok(count)
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] =
    NonEmptyList.of(organizationStatistics, ticketStatistics).reduceK

}
