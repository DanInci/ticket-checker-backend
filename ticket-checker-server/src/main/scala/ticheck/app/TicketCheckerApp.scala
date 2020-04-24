package ticheck.app

import ticheck.effect._
import ticheck.logger._
import ticheck.http.Http4sEC
import busymachines.pureharm.internals.effects.PureharmIOApp
import fs2.Stream
import org.http4s._
import org.http4s.server.Router
import org.http4s.server.ServiceErrorHandler
import org.http4s.server.blaze._
import org.http4s.implicits._
import ticheck.http.middleware.ErrorHandler

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object TicketCheckerApp extends PureharmIOApp {

  //ContextShift[IO] is backed up by a UnsafePools.defaultMainExecutionContext
  override val ioRuntime: Later[(ContextShift[IO], Timer[IO])] =
    IORuntime.defaultMainRuntime("tc-main")

  override def run(args: List[String]): IO[ExitCode] = {
    implicit val concurrentEffect: ConcurrentEffect[IO] = IO.ioConcurrentEffect(contextShift)
    runF[IO](timer, contextShift)
  }

  def runF[F[_]](
    timer:        Timer[F],
    contextShift: ContextShift[F],
  )(implicit F:   ConcurrentEffect[F]): F[ExitCode] = {
    val logger: Logger[F] = Logger.getLogger[F]
    val serverResource: Resource[F, Stream[F, ExitCode]] = for {
      configs <- AllConfigs.defaultR[F]
      pools   <- TCPools.resource[F](contextShift)

      builder = TicketCheckerServerBuilder.build[F](configs)(
        F               = F,
        cs              = contextShift,
        blockingShifter = pools.blockingShifter,
        dbConnEC        = pools.doobieConn,
        dbTransBlocker  = pools.doobieTrans,
      )
      server <- builder.serverResource
      errorHandler = ErrorHandler[F](F, logger)

      serverStream <- serverStreamResource[F](
        config       = server.config,
        routes       = server.routes,
        errorHandler = errorHandler,
        http4sEC     = pools.http4sEC,
      )(
        F     = F,
        timer = timer,
      )
    } yield serverStream

    serverResource
      .use { stream =>
        stream.compile.lastOrError.handleErrorWith { e =>
          logger.error(e)(s"Sever stream error").map(_ => ExitCode.Error)
        }
      }
      .handleErrorWith { e =>
        logger.error(e)(s"Something went wrong w/ server start + and/or shutdown: ${e.toString}").as(ExitCode.Error)
      }
  }

  private def serverStreamResource[F[_]](
    config:       HttpServerConfig,
    routes:       HttpRoutes[F],
    errorHandler: ServiceErrorHandler[F],
    http4sEC:     Http4sEC,
  )(
    implicit
    timer: Timer[F],
    F:     ConcurrentEffect[F],
  ): Resource[F, Stream[F, ExitCode]] = {
    val router  = Router(config.apiRoot -> routes)
    val httpApp = router.orNotFound

    Resource.pure[F, Stream[F, ExitCode]] {
      for {
        server <- BlazeServerBuilder[F]
          .bindHttp(config.port, config.host)
          .withHttpApp(httpApp)
          .withExecutionContext(http4sEC)
          .withServiceErrorHandler(errorHandler)
          .serve
          .covary
      } yield server

    }
  }

}
