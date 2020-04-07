package ticheck.rest.routes

import java.time.LocalDateTime

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.dsl.Http4sDsl
import ticheck.http.{QueryParamInstances, RoutesHelpers}
import ticheck.effect._
import ticheck.organizer.statistic.StatisticOrganizer
import ticheck.rest._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class StatisticRoutes[F[_]](private val statisticOrganizer: StatisticOrganizer[F])(
  implicit val F:                                                                    Async[F],
) extends Http4sDsl[F] with RoutesHelpers with QueryParamInstances {

  object TicketCategoryQueryParamMatcher    extends QueryParamDecoderMatcher[String]("type")
  object StatisticIntervalQueryParamMatcher extends QueryParamDecoderMatcher[String]("interval")
  object StatisticsSizeQueryParamMatcher    extends OptionalQueryParamDecoderMatcher[Int]("size")
  object StatisticsFromQueryParamMatcher    extends OptionalQueryParamDecoderMatcher[LocalDateTime]("from")

  private val statisticRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / `statistics-route` / `organizations-route` / FUUIDVar(orgId) / `tickets-route`
          :? TicketCategoryQueryParamMatcher(ticketCategory) +& StatisticIntervalQueryParamMatcher(statInterval)
            +& StatisticsSizeQueryParamMatcher(statSize) +& StatisticsFromQueryParamMatcher(statFrom) as user =>
      for {
        resp <- Ok()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] = statisticRoutes

}
