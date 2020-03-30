package ticheck.db

import busymachines.pureharm.db

import ticheck.logger._
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait FlywayAlgebra[F[_]] {
  def runMigrations(implicit logger: Logger[F]): F[Int]

  def cleanDB(implicit logger: Logger[F]): F[Unit]
}

object FlywayAlgebra {

  def apply[F[_]: Sync](
    jdbcUrl:    JDBCUrl,
    dbUser:     DBUsername,
    dbPassword: DBPassword,
    config:     Option[db.flyway.FlywayConfig] = Option.empty,
  ): FlywayAlgebra[F] = new FlywayAlgebraImpl[F](jdbcUrl, dbUser, dbPassword, config)

  final private class FlywayAlgebraImpl[F[_]](
    private[FlywayAlgebra] val jdbcUrl:    JDBCUrl,
    private[FlywayAlgebra] val dbUser:     DBUsername,
    private[FlywayAlgebra] val dbPassword: DBPassword,
    private[FlywayAlgebra] val config:     Option[db.flyway.FlywayConfig],
  )(
    implicit private val F: Sync[F],
  ) extends FlywayAlgebra[F] {

    override def runMigrations(implicit logger: Logger[F]): F[Int] = {
      val mig = db.flyway.Flyway.migrate[F](
        jdbcUrl,
        dbUser,
        dbPassword,
        config,
      )
      mig.flatTap(migs => logger.info(s"Successfully applied: $migs flyway migrations"))
    }

    override def cleanDB(implicit logger: Logger[F]): F[Unit] = {
      val mig = db.flyway.Flyway.clean[F](
        url      = jdbcUrl,
        username = dbUser,
        password = dbPassword,
      )
      logger.warn(s"CLEANING DB: $jdbcUrl — better make sure this isn't on prod, lol") >> mig
    }
  }

}
