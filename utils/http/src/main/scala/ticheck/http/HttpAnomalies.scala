package ticheck.http

import ticheck._
import ticheck.effect._

import io.circe.{CursorOp, DecodingFailure}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final case class InvalidJSONBodyAnomaly(cause: DecodingFailure)
    extends InvalidInputAnomaly(s"Invalid json body. ${cause.getMessage}") {
  override val id: AnomalyID = AnomalyIDs.InvalidJSONBodyID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    Seq(
      "path" -> Anomaly.Parameter(CursorOp.opsToPath(cause.history)),
    ) ++
      cause.history.zipWithIndex.map { t =>
        val (op, idx) = t
        s"op$idx" -> Anomaly.Parameter(op.show)
      }: _*,
  )
}

final case class KnownInvalidMessageAnomaly(msg: String, cause: Throwable)
    extends InvalidInputAnomaly(s"Invalid HTTP request. Message: $msg >> $cause") {
  override val id: AnomalyID = AnomalyIDs.InvalidHTTPRequestID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message" -> msg,
    "cause"   -> cause.toString,
  )

}

final case class InvalidMessageAnomaly(msg: String)
    extends InvalidInputAnomaly(s"Invalid HTTP request. Message: $msg") {
  override val id: AnomalyID = AnomalyIDs.InvalidHTTPRequestID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "message" -> msg,
  )

}
