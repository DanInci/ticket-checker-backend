package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
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
