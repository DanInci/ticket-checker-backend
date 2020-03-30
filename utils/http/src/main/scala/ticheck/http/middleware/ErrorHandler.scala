package ticheck.http.middleware

import ticheck._
import ticheck.effect._
import ticheck.logger._
import ticheck.json._
import ticheck.http._

import ticheck.http.Http4sCirceInstances

import org.http4s.{Challenge, Header, Headers, MessageFailure, Response, Status}
import org.http4s.headers.{`Content-Length`, `WWW-Authenticate`, Connection}
import org.http4s.server.ServiceErrorHandler
import org.http4s.util.CaseInsensitiveString

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object ErrorHandler extends Http4sCirceInstances {

  private val challenges: NonEmptyList[Challenge] = NonEmptyList.of(
    Challenge(
      scheme = "Bearer",
      realm  = "HealthFan",
    ),
  )

  private lazy val wwwAuthenticateHeader: Header = `WWW-Authenticate`(challenges)

  def apply[F[_]](implicit F: Monad[F], logger: Logger[F]): ServiceErrorHandler[F] = req => {
    case e: InvalidInputAnomaly =>
      for {
        _ <- logger.info(s"Invalid Input Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr
          .getOrElse("<unknown>")}. Message: ${e.message}")
        resp <- F.pure(
          Response[F](status = Status.BadRequest, httpVersion = req.httpVersion)
            .withEntity(e: AnomalyBase),
        )
      } yield resp.putHeaders(cors: _*)
    case e: UnauthorizedAnomaly =>
      for {
        _ <- logger.info(s"Unauthorized Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr
          .getOrElse("<unknown>")}. Message: ${e.message}")
        resp <- F.pure {
          Response[F](
            status      = Status.Unauthorized,
            httpVersion = req.httpVersion,
            headers     = Headers.of(wwwAuthenticateHeader),
          ).withEntity(e: AnomalyBase)
        }
      } yield resp.putHeaders(cors: _*)
    case e: ForbiddenAnomaly =>
      for {
        _ <- logger.info(s"Forbidden Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr
          .getOrElse("<unknown>")}. Message: ${e.message}")
        resp <- F.pure(
          Response[F](status = Status.Forbidden, httpVersion = req.httpVersion)
            .withEntity(e: AnomalyBase),
        )
      } yield resp.putHeaders(cors: _*)
    case e: NotFoundAnomaly =>
      for {
        _ <- logger.info(
          s"Not Found Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr.getOrElse("<unknown>")}. Message: ${e.message}",
        )
        resp <- F.pure(
          Response[F](status = Status.NotFound, httpVersion = req.httpVersion)
            .withEntity(e: AnomalyBase),
        )
      } yield resp.putHeaders(cors: _*)
    case e: ConflictAnomaly =>
      for {
        _ <- logger.info(
          s"Conflict Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr.getOrElse("<unknown>")}. Message: ${e.message}",
        )
        resp <- F.pure(
          Response[F](status = Status.Conflict, httpVersion = req.httpVersion)
            .withEntity(e: AnomalyBase),
        )
      } yield resp.putHeaders(cors: _*)
    case e: InconsistentState =>
      for {
        _ <- logger.error(
          s"Inconsistent State Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr
            .getOrElse("<unknown>")}. Message: ${e.message}",
        )
        resp <- F.pure(
          Response[F](
            status      = Status.InternalServerError,
            httpVersion = req.httpVersion,
            Headers.of(Connection(CaseInsensitiveString("close")), `Content-Length`.zero),
          ).withEntity(e: AnomalyBase),
        )
      } yield resp.putHeaders(cors: _*)

    //FIXME: for some reason, this usually gets converted to a failed response faster
    //FIXME: so it never actually gets his. Check to see if authmiddleware, or rhomiddleware
    //FIXME: doesn't reify the error into a response before this is applied
    case mf: MessageFailure =>
      val anomaly: AnomalyBase = mf.cause match {
        case Some(cdf: io.circe.DecodingFailure) => InvalidJSONBodyAnomaly(cdf)
        case Some(e) => KnownInvalidMessageAnomaly(mf.getMessage(), e)
        case None    => InvalidMessageAnomaly(mf.getMessage())
      }
      val anomalyJSON = anomaly.asJson
      for {

        _ <- logger.info(
          s"Message Failure: ${req.method} ${req.pathInfo} from ${req.remoteAddr.getOrElse("<unknown>")}. Anomaly >> ${anomalyJSON.noSpaces}",
        )
        resp = mf.toHttpResponse[F](req.httpVersion)
      } yield resp.putHeaders(cors: _*).withEntity(anomalyJSON)

    case t if !t.isInstanceOf[VirtualMachineError] =>
      for {
        _ <- logger.error(t)(
          message = s"Servicing request: ${req.method} ${req.pathInfo} from ${req.remoteAddr.getOrElse("<unknown>")}",
        )
        resp <- F.pure(
          Response[F](
            status      = Status.InternalServerError,
            httpVersion = req.httpVersion,
            headers     = Headers.of(Connection(CaseInsensitiveString("close")), `Content-Length`.zero),
          ),
        )
      } yield resp.putHeaders(cors: _*)

  }

}
