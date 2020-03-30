package ticheck.http.middleware

import ticheck.effect._
import ticheck.logger._

import io.chrisdavenport.log4cats.StructuredLogger
import org.http4s.{HttpRoutes, Request, Response}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object LogMiddleware {
  private val ReqIDKey  = "REQ_ID"
  private val ReqURLKey = "REQ_URL"

  def logServices[F[_]: Logger: Timer: Sync](routes: HttpRoutes[F]): HttpRoutes[F] = {
    Kleisli[OptionT[F, ?], Request[F], Response[F]] { r: Request[F] =>
      val logger = StructuredLogger[F]
      val reqS   = s"${r.method}: ${r.uri}"
      for {
        random <- OptionT.liftF(Sync[F].delay(Math.abs(scala.util.Random.nextLong())))
        m = Map[String, String](
          ReqIDKey  -> random.toString,
          ReqURLKey -> reqS,
        )
        _    <- OptionT.liftF(logger.debug(ctx = m)(s"received: $reqS"))
        resp <- routes(r)
        _    <- OptionT.liftF(logger.debug(ctx = m)(s"finished: $reqS — Status=${resp.status}"))
      } yield resp
    }
  }

}
