package ticheck.dao.organization.membership

import ticheck.{Anomaly, AnomalyID, AnomalyIDs, InvalidInputAnomaly}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */

final case class InvalidOrganizationRoleAnomaly(rawRole: String)
    extends InvalidInputAnomaly(
      s"Invalid organization role string representation: $rawRole",
    ) {
  override val id: AnomalyID = AnomalyIDs.InvalidOrganizationRoleID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message" -> "Invalid organization role string representation",
    "role"    -> rawRole,
  )
}
