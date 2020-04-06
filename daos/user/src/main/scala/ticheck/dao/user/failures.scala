package ticheck.dao.user

import ticheck._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
final case class InvalidEmailAnomaly(rawEmail: String)
    extends InvalidInputAnomaly(s"Invalid email address: $rawEmail") {
  override val id: AnomalyID = AnomalyIDs.InvalidEmailAddressID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message" -> "Invalid email address",
    "email"   -> rawEmail,
  )

}

final case class InvalidUserRoleAnomaly(rawRole: String)
    extends InvalidInputAnomaly(
      s"Invalid UserRole string representation: $rawRole",
    ) {
  override val id: AnomalyID = AnomalyIDs.InvalidUserRoleID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message" -> "Invalid UserRole string representation",
    "role"    -> rawRole,
  )
}
