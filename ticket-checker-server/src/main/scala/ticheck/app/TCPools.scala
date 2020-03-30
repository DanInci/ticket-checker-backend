package ticheck.app

import ticheck.effect._
import ticheck.db._
import ticheck.http.Http4sEC

/**
  *
  * This represents all pools in our application + utilities.
  *
  * In total, our application manages 5 thread pools. 4 of which
  * you see here, and one that backs up the [[ContextShift]] given
  * as parameter.
  *
  * @param doobieConn
  *   Doobie manages its connections to the DB for it. by default it's #of CPUs * 3
  *   see documentation on [[Pools.fixed]]
  * @param doobieTrans
  *   Actualy queries and data fetching block these threads, that's why it is a
  *   cached thread pool. See [[Pools.cached]]
  *  @param http4sEC
  *   HTTP4s does its stuff on this pool. It is a [[Pools.fixed]] in order to back-pressure
  *   the client, so that it never overfloods our app. This is why the number of connections
  *   to the DB is larger than the number of threads allocated to this one. By limiting the number
  *   of http requests we naturally get backpressure. Otherwise we'd be fucked beyond belief (this
  *   is what happens in literally all our other apps where we have an ExecutionContext.global)
  *   doing everything, lol.
  * @param contextShift
  *  If initialized the pureharm way, in your main app (unfortunately, there we have to do it unsafe)
  *  via the [[IORuntime.defaultMainRuntime]], this is the ContextShift that runs pretty much all
  *  CPU computation in our app, all .map, .flatMap, etc., everything that is not explicitly
  *  shifted in the doobie, http4s, or by the [[blockingShifter]].
  * @param blockingShifter
  *   util that combines [[externalIO]] with [[contextShift]] for easily putting blocking computation
  *   on a dedicated thread pool, and not having to worry about resource safety.
  * @param externalIO
  *   all blocking i/o that is not explicitly managed by doobie, or http4s
  *   should be done here. Examples: communicating with AWS, expensive logging,
  *   external http calls, etc. BUT, this pool is a low level
  *   construct, and should be used via the [[BlockingShifter]], rather than the externalIO
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final private[app] class TCPools[F[_]] private (
  val doobieConn:           DoobieConnectionEC,
  val doobieTrans:          DoobieBlocker,
  val http4sEC:             Http4sEC,
  val contextShift:         ContextShift[F],
  val blockingShifter:      BlockingShifter[F],
  protected val externalIO: ExecutionContextCT,
)

object TCPools {

  def resource[F[_]: Sync](mainCS: ContextShift[F]): Resource[F, TCPools[F]] =
    for {
      availableCPUs: Int                <- Pools.availableCPUs[F]
      externalIO:    ExecutionContextCT <- Pools.cached[F]("tc-block")
      doobieConn:    DoobieConnectionEC <- Pools.fixed[F]("tc-doobie-conn", availableCPUs * 3).map(DoobieConnectionEC.safe)
      doobieTrans:   DoobieBlocker      <- Pools.cached[F]("tc-doobie-trans").map(DoobieBlocker.safe)
      http4sEC:      Http4sEC           <- Pools.fixed[F]("tc-http4s", availableCPUs).map(Http4sEC.safe)

      blockingShifter: BlockingShifter[F] <- Resource.pure[F, BlockingShifter[F]](
        BlockingShifter.fromExecutionContext[F](externalIO)(mainCS),
      )
    } yield new TCPools[F](
      doobieConn      = doobieConn,
      doobieTrans     = doobieTrans,
      http4sEC        = http4sEC,
      contextShift    = mainCS,
      blockingShifter = blockingShifter,
      externalIO      = externalIO,
    )

}
