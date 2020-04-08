package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object AnomalyIDs {

  case object InconsistentStateAnomalyID extends AnomalyID { override val name: String = "ISCATA" }
  case object InvalidEmailAddressID      extends AnomalyID { override val name: String = "IV_EMAIL" }

  //--------------------------- HTTP --------------------------------
  case object InvalidJSONBodyID    extends AnomalyID { override val name: String = "IV_JSONB" }
  case object InvalidHTTPRequestID extends AnomalyID { override val name: String = "IV_MSG" }

  //--------------------------- AUTH  ---------------------------
  case object AuthenticationFailedAnomalyID extends AnomalyID { override val name: String = "UA" }
  case object InvalidPasswordAnomalyID      extends AnomalyID { override val name: String = "IV_PASS" }
  case object ConflictEmailExistsID         extends AnomalyID { override val name: String = "CF_EMAIL" }
  case object JWTVerificationAnomalyID      extends AnomalyID { override val name: String = "IVJWT" }
  case object JWTAuthCtxMalformedAnomalyID  extends AnomalyID { override val name: String = "IVJWTMAL" }

  //--------------------------- AUTH HTTP ---------------------------
  case object MissingXAuthTokenHeaderID extends AnomalyID { override val name: String = "UA_MISSING_XAUTH_HEADER" }

  //--------------------------- ORGANIZATION MEMBERSHIP DAO -------------------------------
  case object InvalidOrganizationRoleID extends AnomalyID { override val name: String = "IV_ORG_ROLE" }

  //--------------------------- ORGANIZATION INVITE DAO -------------------------------
  case object InvalidInviteStatusID extends AnomalyID { override val name: String = "IV_INV_STATUS" }

  //------------------------------- TICKET ALGEBRA ---------------------------------------
  case object InvalidTicketCategoryID extends AnomalyID { override val name: String = "IV_TICKET_CAT" }
  case object InvalidIntervalTypeID   extends AnomalyID { override val name: String = "IV_INTERVAL_TYP" }

}
