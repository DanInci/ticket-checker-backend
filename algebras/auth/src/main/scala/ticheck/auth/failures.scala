package ticheck.auth

import ticheck._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
case object LoginFailedAnomaly extends UnauthorizedAnomaly("Login attempt has failed") {
  override val id: AnomalyID = AnomalyIDs.AuthenticationFailedAnomalyID
}

case object PasswordDoesNotMeetCriteriaAnomaly
    extends InvalidInputAnomaly(
      s"Password must be at least 6 characters long, including an uppercase and a lowercase letter",
    ) {
  override val id: AnomalyID = AnomalyIDs.InvalidPasswordAnomalyID
}
case class EmailAlreadyRegisteredAnomaly(email: Email)
    extends ConflictAnomaly(
      s"Email '$email' is already registered to another account",
    ) {
  override val id: AnomalyID = AnomalyIDs.ConflictEmailExistsID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "email" -> email,
  )
}

case object JWTVerificationAnomaly extends UnauthorizedAnomaly("Invalid JWT token.") {
  override val id: AnomalyID = AnomalyIDs.JWTVerificationAnomalyID
}

/**
  * At this point it's ok to give more detailed explanations, since this is about
  * how we encode business logic in our JWT claims, and is always done if the JWT
  * token is properly signed.
  */
case class JWTAuthCtxMalformedAnomaly(msg: String)
    extends UnauthorizedAnomaly(s"JWT custom claim: authCtx is malformed: $msg") {
  override val id: AnomalyID = AnomalyIDs.JWTAuthCtxMalformedAnomalyID
}
