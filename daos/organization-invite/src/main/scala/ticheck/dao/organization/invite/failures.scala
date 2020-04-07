package ticheck.dao.organization.invite

import ticheck.{Anomaly, AnomalyID, InvalidInputAnomaly}
import ticheck.AnomalyIDs

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class InvalidInviteStatusAnomaly(rawStatus: String)
    extends InvalidInputAnomaly(
      s"Invalid invite status string representation: $rawStatus",
    ) {
  override val id: AnomalyID = AnomalyIDs.InvalidInviteStatusID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message" -> "Invalid invite status string representation",
    "status"  -> rawStatus,
  )
}
