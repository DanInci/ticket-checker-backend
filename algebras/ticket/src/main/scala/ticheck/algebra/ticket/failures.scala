package ticheck.algebra.ticket

import ticheck.{Anomaly, AnomalyID, InvalidInputAnomaly}
import ticheck.AnomalyIDs

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class InvalidTicketCategoryAnomaly(rawCategory: String)
    extends InvalidInputAnomaly(
      s"Invalid ticket category string representation: $rawCategory",
    ) {
  override val id: AnomalyID = AnomalyIDs.InvalidTicketCategoryID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message"  -> "Invalid ticket category string representation",
    "category" -> rawCategory,
  )
}

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
