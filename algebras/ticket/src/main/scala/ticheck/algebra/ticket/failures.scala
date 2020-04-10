package ticheck.algebra.ticket

import busymachines.pureharm.anomaly.{ConflictAnomaly, NotFoundAnomaly}
import ticheck.{Anomaly, AnomalyID, AnomalyIDs, InvalidInputAnomaly, OrganizationID, TicketID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class InvalidIntervalTypeAnomaly(rawInterval: String)
    extends InvalidInputAnomaly(
      s"Invalid interval type string representation: $rawInterval",
    ) {
  override val id: AnomalyID = AnomalyIDs.InvalidIntervalTypeID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message"  -> "Invalid interval type string representation",
    "category" -> rawInterval,
  )
}

final case class TicketNFA(organizationId: OrganizationID, ticketId: TicketID)
    extends NotFoundAnomaly(
      s"Ticket with id '$ticketId' for organization with id '${organizationId.show}' was not found",
    ) {
  override val id: AnomalyID = AnomalyIDs.TicketNotFoundID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "ticketId"       -> TicketID.despook(ticketId),
    "organizationId" -> organizationId.show,
  )
}

final case class TicketAlreadyExistsCA(organizationId: OrganizationID, ticketId: TicketID)
    extends ConflictAnomaly(
      s"Ticket with id '$ticketId' for organization with id '${organizationId.show}' already exists",
    ) {
  override val id: AnomalyID = AnomalyIDs.TicketAlreadyExistsID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "ticketId"       -> TicketID.despook(ticketId),
    "organizationId" -> organizationId.show,
  )
}

final case class TicketAlreadyValidatedCA(organizationId: OrganizationID, ticketId: TicketID)
    extends ConflictAnomaly(
      s"Ticket with id '$ticketId' for organization with id '${organizationId.show}' is already validated",
    ) {
  override val id: AnomalyID = AnomalyIDs.TicketAlreadyValidatedID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "ticketId"       -> TicketID.despook(ticketId),
    "organizationId" -> organizationId.show,
  )
}

final case class TicketAlreadyNotValidatedCA(organizationId: OrganizationID, ticketId: TicketID)
    extends ConflictAnomaly(
      s"Ticket with id '$ticketId' for organization with id '${organizationId.show}' is already not validated",
    ) {
  override val id: AnomalyID = AnomalyIDs.TicketAlreadyNotValidatedID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "ticketId"       -> TicketID.despook(ticketId),
    "organizationId" -> organizationId.show,
  )
}
