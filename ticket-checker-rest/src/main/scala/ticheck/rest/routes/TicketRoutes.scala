package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}
import ticheck.effect._
import ticheck.rest._
import ticheck.http._
import org.http4s.dsl.Http4sDsl
import ticheck.algebra.ticket.TicketCategory
import ticheck.algebra.ticket.models.{TicketDefinition, TicketUpdateDefinition}
import ticheck.{OrganizationID, PagingInfo, TicketID, UserID}
import ticheck.http.{QueryParamInstances, RoutesHelpers}
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
) extends Http4sDsl[F] with RoutesHelpers with QueryParamInstances {

  implicit val ticketCategoryQueryParamDecoder: QueryParamDecoder[TicketCategory] =
    (value: QueryParameterValue) =>
      TicketCategory
        .fromString(value.value)
        .leftMap(t => ParseFailure("Query param decoding failed", t.getMessage))
        .toValidatedNel

  object CategoryQueryParamMatcher extends OptionalQueryParamDecoderMatcher[TicketCategory]("category")
  object UserIDQueryParamMatcher   extends OptionalQueryParamDecoderMatcher[UserID]("userId")
  object SearchQueryParamMatcher   extends OptionalQueryParamDecoderMatcher[String]("search")

  private val organizationTicketsRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case (req @ POST -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route`) as user =>
      for {
        definition <- req.as[TicketDefinition]
        ticket     <- ticketOrganizer.createTicket(OrganizationID.spook(oid), definition)(user)
        resp       <- Created(ticket)
      } yield resp

    case GET -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route`
          :? CategoryQueryParamMatcher(category) +& UserIDQueryParamMatcher(uid) +& SearchQueryParamMatcher(searchValue)
            +& PageOffsetMatcher(offset) +& PageLimitMatcher(limit) as user =>
      for {
        tickets <- ticketOrganizer
          .getOrganizationTickets(OrganizationID.spook(oid), PagingInfo(offset, limit), category, uid, searchValue)(
            user,
          )
        resp <- Ok(tickets)
      } yield resp

    case GET -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId as user =>
      for {
        ticket <- ticketOrganizer.getTicket(OrganizationID.spook(oid), TicketID.spook(ticketId))(user)
        resp   <- Ok(ticket)
      } yield resp

    case (req @ PUT -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId) as user =>
      for {
        definition <- req.as[TicketUpdateDefinition]
        ticket     <- ticketOrganizer.updateTicket(OrganizationID.spook(oid), TicketID.spook(ticketId), definition)(user)
        resp       <- Ok(ticket)
      } yield resp

    case POST -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId / "validate" as user =>
      for {
        ticket <- ticketOrganizer
          .setTicketValidationStatus(OrganizationID.spook(oid), TicketID.spook(ticketId), isValid = true)(user)
        resp <- Ok(ticket)
      } yield resp

    case POST -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId / "invalidate" as user =>
      for {
        ticket <- ticketOrganizer
          .setTicketValidationStatus(OrganizationID.spook(oid), TicketID.spook(ticketId), isValid = false)(user)
        resp <- Ok(ticket)
      } yield resp

    case DELETE -> Root / `organizations-route` / FUUIDVar(oid) / `tickets-route` / ticketId as user =>
      for {
        _    <- ticketOrganizer.deleteTicket(OrganizationID.spook(oid), TicketID.spook(ticketId))(user)
        resp <- NoContent()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] = organizationTicketsRoutes

}
