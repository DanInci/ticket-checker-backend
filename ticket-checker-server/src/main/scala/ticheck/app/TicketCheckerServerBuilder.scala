package ticheck.app

import org.http4s.HttpRoutes
import ticheck.effect._
import ticheck.db._
import ticheck.logger._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait TicketCheckerServerBuilder[F[_]] {
  def serverResource: Resource[F, TicketCheckerServerBuilder.TicketCheckerServer[F]]
}

object TicketCheckerServerBuilder {

  case class TicketCheckerServer[G[_]](
    config: HttpServerConfig,
    module: ModuleTicketChecker[G],
    routes: HttpRoutes[G],
  )

  def build[F[_]](
    allConfigs: AllConfigs,
  )(
    implicit
    F:               Concurrent[F],
    timer:           Timer[F],
    cs:              ContextShift[F],
    blockingShifter: BlockingShifter[F],
    dbConnEC:        DoobieConnectionEC,
    dbTransBlocker:  DoobieBlocker,
  ): TicketCheckerServerBuilder[F] = {
    new TicketCheckerServerBuilderImpl[F](allConfigs)
  }

  final private class TicketCheckerServerBuilderImpl[F[_]] private[TicketCheckerServerBuilder] (
    allConfigs: AllConfigs,
  )(
    implicit
    private val F:              Concurrent[F],
    private val timer:          Timer[F],
    private val contextShift:   ContextShift[F],
    private val shifter:        BlockingShifter[F],
    private val dbConnEC:       DoobieConnectionEC,
    private val dbTransBlocker: DoobieBlocker,
  ) extends TicketCheckerServerBuilder[F] {
    implicit private val logger: Logger[F] = Logger.getLogger[F]

    override def serverResource: Resource[F, TicketCheckerServer[F]] =
      for {
        server <- initServer(allConfigs)
      } yield server

    private def initServer(
      ac: AllConfigs,
    ): Resource[F, TicketCheckerServer[F]] = {
      for {
        transactor    <- DatabaseAlgebra.transactor[F](ac.dbConfig, dbConnEC, dbTransBlocker)
        flyWayAlgebra <- flywayAlgebra(ac.dbConfig)
        _             <- Resource.liftF[F, Int](flyWayAlgebra.runMigrations)

        mkModuleF: F[ModuleTicketChecker[F]] = ModuleTicketChecker.concurrent[F](ac)(
          c          = F,
          tim        = timer,
          t          = transactor,
          cs         = contextShift,
          bioShifter = shifter,
        )

        cleanModuleF: F[Unit] = F.unit

        tcModule <- Resource.make[F, ModuleTicketChecker[F]](mkModuleF)((_: ModuleTicketChecker[F]) => cleanModuleF)
        routes   <- Resource.liftF(tcModule.routes)
      } yield TicketCheckerServer[F](
        config = ac.serverConfig,
        module = tcModule,
        routes = routes,
      )
    }

    private def flywayAlgebra(dbConfig: DBConfig): Resource[F, FlywayAlgebra[F]] =
      Resource.pure[F, FlywayAlgebra[F]](
        /*_*/
        FlywayAlgebra[F](
          jdbcUrl    = dbConfig.url,
          dbUser     = dbConfig.username,
          dbPassword = dbConfig.password,
        ),
        /*_*/
      )
  }
}
