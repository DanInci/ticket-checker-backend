package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.QueryParamDecoder
import ticheck.effect._
import ticheck.rest._
import org.http4s.dsl.Http4sDsl
import ticheck.{FUUID, UserID}
import ticheck.http.RoutesHelpers
import ticheck.organizer.ticket.TicketOrganizer
import ticheck.rest.UserAuthCtxRoutes

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] case class TicketRoutes[F[_]](private val ticketOrganizer: TicketOrganizer[F])(
  implicit val F:                                                              Async[F],
) extends Http4sDsl[F] with RoutesHelpers {

  implicit val userIdQueryParamMatcher: QueryParamDecoder[UserID] = phantomTypeQueryParamDecoder[F, FUUID, UserID.Tag]

  object CategoryQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("category")
  object UserIDQueryParamMatcher   extends OptionalQueryParamDecoderMatcher[UserID]("userId")
  object SearchQueryParamMatcher   extends OptionalQueryParamDecoderMatcher[String]("search")

  private val organizationTicketsRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case req @ POST -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` as user =>
      for {
        resp <- Created()
      } yield resp

    case GET -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route`
          :? CategoryQueryParamMatcher(cat) +& UserIDQueryParamMatcher(uid) +& SearchQueryParamMatcher(searchStr) as user =>
      for {
        resp <- Ok()
      } yield resp

    case GET -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId as user =>
      for {
        resp <- Ok()
      } yield resp

    case req @ PUT -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId as user =>
      for {
        resp <- Ok()
      } yield resp

    case req @ POST -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId / "validate" as user =>
      for {
        resp <- Ok()
      } yield resp

    case req @ POST -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId / "invalidate" as user =>
      for {
        resp <- Ok()
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId as user =>
      for {
        resp <- NoContent()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] = organizationTicketsRoutes

}
