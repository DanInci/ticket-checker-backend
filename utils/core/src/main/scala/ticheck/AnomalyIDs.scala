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

}
