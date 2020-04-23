package ticheck.app.static

import ticheck.effect._
import org.http4s.CacheDirective.`no-cache`
import org.http4s.{HttpRoutes, Request, Response, StaticFile}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Cache-Control`

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/23/2020
  *
  */
object StaticFiles {

  def routes[F[_]: Sync: ContextShift](
    implicit shifter: BlockingShifter[F],
  ): F[HttpRoutes[F]] =
    Sync[F].delay {
      val fdsl = Http4sDsl[F]
      import fdsl._

      val imagesRoute               = s"images"
      val imagesDirectory           = "/images"
      val supportedStaticExtensions = List(".png", ".ico", ".jpg", ".jpeg")

      def fetchResource(path: String, req: Request[F]): F[Response[F]] = {
        StaticFile
          .fromResource(path, shifter.blocker, Some(req))
          .map(_.putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
          .getOrElseF(NotFound())
      }

      HttpRoutes.of[F] {
        case req @ GET -> Root / `imagesRoute` / file if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
          val staticFilePath = s"$imagesDirectory/$file"
          fetchResource(staticFilePath, req)
      }
    }

}
