package ticheck.dao.ticket

import ticheck._
import ticheck.AnomalyIDs

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/9/2020
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
