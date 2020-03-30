package ticheck.db

import doobie.hikari.HikariTransactor

import ticheck.effect._
import ticheck.logger._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object DatabaseAlgebra {

  def transactor[F[_]: Async: ContextShift](
    dbConfig:  DBConfig,
    dbConnEC:  DoobieConnectionEC,
    dbBlocker: DoobieBlocker,
  ): Resource[F, Transactor[F]] = {
    for {
      transactor <- HikariTransactor.newHikariTransactor[F](
        driverClassName = "org.postgresql.Driver",
        url             = dbConfig.url,
        user            = dbConfig.username,
        pass            = dbConfig.password,
        connectEC       = dbConnEC,
        blocker         = dbBlocker,
      )
    } yield transactor
  }

  def initializeDB[F[_]: Logger: Sync](flywayAlgebra: FlywayAlgebra[F]): Resource[F, Int] = {
    Resource.liftF[F, Int](flywayAlgebra.runMigrations)
  }

}
