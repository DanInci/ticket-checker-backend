package ticheck.auth

import ticheck._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
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
