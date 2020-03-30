package ticheck.db

import ticheck.effect.Sync
import ticheck.config._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
case class DBConfig(
  url:                JDBCUrl,
  username:           DBUsername,
  password:           DBPassword,
  migrationLocations: List[DBMigrationLocation],
)

object DBConfig extends ConfigLoader[DBConfig] {

  implicit override val configReader: ConfigReader[DBConfig] = semiauto.deriveReader[DBConfig]

  override def default[F[_]: Sync]: F[DBConfig] = {
    this.load[F]("ticket-checker.db")
  }
}
