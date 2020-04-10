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
  case object MissingXAuthTokenHeaderID extends AnomalyID { override val name: String = "UA_MISSING_AUTH_HEADER" }

  //--------------------------- ORGANIZATION MEMBERSHIP DAO -------------------------------
  case object InvalidOrganizationRoleID extends AnomalyID { override val name: String = "IV_ORG_ROLE" }

  //--------------------------- ORGANIZATION INVITE DAO -------------------------------
  case object InvalidInviteStatusID extends AnomalyID { override val name: String = "IV_INV_STATUS" }

  //---------------------------------- TICKET DAO -------------------------------------------
  case object InvalidTicketCategoryID extends AnomalyID { override val name: String = "IV_TIC_CAT" }

  //-------------------------------- USER ALGEBRA ----------------------------------------
  case object UserNotFoundID            extends AnomalyID { override val name: String = "USR_NFA_01" }
  case object UserIsOrganizationOwnerID extends AnomalyID { override val name: String = "USR_CA_02" }

  //------------------------------ ORGANIZATION ALGEBRA ----------------------------------------
  case object OrganizationNotFoundID             extends AnomalyID { override val name: String = "ORG_NFA_01" }
  case object OrganizationAlreadyExistsID        extends AnomalyID { override val name: String = "ORG_CA_02" }
  case object OrganizationInviteNotFoundID       extends AnomalyID { override val name: String = "ORG_NFA_03" }
  case object OrganizationInviteIsAnsweredID     extends AnomalyID { override val name: String = "ORG_CA_04" }
  case object OrganizationInviteExistsID         extends AnomalyID { override val name: String = "ORG_CA_05" }
  case object OrganizationMemberExistsID         extends AnomalyID { override val name: String = "ORG_CA_06" }
  case object OrganizationMemberNotFoundID       extends AnomalyID { override val name: String = "ORG_NFA_07" }
  case object OrganizationMemberRoleNotAllowedID extends AnomalyID { override val name: String = "ORG_IIA_08" }

  //------------------------------- TICKET ALGEBRA ---------------------------------------
  case object InvalidIntervalTypeID       extends AnomalyID { override val name: String = "IV_INTERVAL_TYP" }
  case object TicketNotFoundID            extends AnomalyID { override val name: String = "TIC_NFA_01" }
  case object TicketAlreadyExistsID       extends AnomalyID { override val name: String = "TIC_CA_02" }
  case object TicketAlreadyValidatedID    extends AnomalyID { override val name: String = "TIC_CA_03" }
  case object TicketAlreadyNotValidatedID extends AnomalyID { override val name: String = "TIC_CA_04" }

}
