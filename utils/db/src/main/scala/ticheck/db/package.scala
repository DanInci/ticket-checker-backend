package ticheck

import ticheck.effect._
import busymachines.pureharm

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object db
    extends pureharm.db.PureharmDBCoreTypeDefinitions with doobie.free.Instances with doobie.syntax.AllSyntax
    with doobie.postgres.Instances with doobie.postgres.free.Instances
    with doobie.postgres.syntax.ToPostgresMonadErrorOps with doobie.postgres.syntax.ToFragmentOps {

  /**
    * Denotes the EC on which connections are managed,
    * backed up by a fixed thread pool with the number of threads
    * equal to the number of connections
    */
  object DoobieConnectionEC extends PhantomType[ExecutionContext] {
    def safe(ec: ExecutionContextFT): this.Type = this.apply(ec)
  }

  //TODO: move to pureharm-db-doobie when implemented
  type DoobieConnectionEC = DoobieConnectionEC.Type

  /**
    * Denotes the EC on which transactions(dbops) are managed,
    * backed up by a cached thread pool because blocking
    * i/o is executed on this one
    */
  type DoobieBlocker = DoobieBlocker.Type

  object DoobieBlocker extends PhantomType[Blocker] {
    def safe(ec: ExecutionContextCT): this.Type = this(Blocker.liftExecutionContext(ec))
  }

  object DBMigrationLocation extends PhantomType[String]
  type DBMigrationLocation = DBMigrationLocation.Type

  type Meta[A] = doobie.Meta[A]
  val Meta: doobie.Meta.type = doobie.Meta

  type Transactor[F[_]] = doobie.Transactor[F]
  val Transactor: doobie.Transactor.type = doobie.Transactor

  type ConnectionIO[A] = doobie.ConnectionIO[A]
  val ConnectionIO: cats.effect.Async[ConnectionIO] = doobie.implicits.AsyncConnectionIO

}
