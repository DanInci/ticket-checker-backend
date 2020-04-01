package ticheck.auth.http

import ticheck._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
case object MissingXAuthTokenHeaderAnomaly extends UnauthorizedAnomaly(s"No ${AuthedHttp4s.`X-Auth-Token`} provided") {
  override val id: AnomalyID = AnomalyIDs.MissingXAuthTokenHeaderID
}
