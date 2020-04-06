package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object AnomalyIDs {

  case object InconsistentStateAnomalyID extends AnomalyID { override val name: String = "ISCATA" }

  //--------------------------- HTTP --------------------------------
  case object InvalidJSONBodyID    extends AnomalyID { override val name: String = "IV_JSONB" }
  case object InvalidHTTPRequestID extends AnomalyID { override val name: String = "IV_MSG" }

  //--------------------------- AUTH  ---------------------------
  case object JWTVerificationAnomalyID     extends AnomalyID { override val name: String = "IVJWT" }
  case object JWTAuthCtxMalformedAnomalyID extends AnomalyID { override val name: String = "IVJWTMAL" }

  //--------------------------- AUTH HTTP ---------------------------
  case object MissingXAuthTokenHeaderID extends AnomalyID { override val name: String = "UA_MISSING_XAUTH_HEADER" }

  //--------------------------- USER DAO -------------------------------
  case object InvalidEmailAddressID extends AnomalyID { override val name: String = "IV_EMAIL" }
  case object InvalidUserRoleID     extends AnomalyID { override val name: String = "IV_ROLE" }
}
