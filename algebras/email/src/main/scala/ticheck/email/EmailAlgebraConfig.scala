package ticheck.email

import ticheck._
import ticheck.effect._
import ticheck.config._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
final case class EmailAlgebraConfig(
  emailFrom:    Email,
  smtpUsername: SMTPUsername,
  smtpPassword: SMTPPlainTextPassword,
  smtpHostName: SMTPHostName,
  smtpPort:     SMTPPort,
)

object EmailAlgebraConfig extends ConfigLoader[EmailAlgebraConfig] {

  implicit val emailConfigReader: ConfigReader[Email] =
    ConfigReader[String].emap[Email] { s: String =>
      Email(s).leftMap(e => pureconfig.error.ExceptionThrown(e): pureconfig.error.FailureReason)
    }
  implicit override val configReader: ConfigReader[EmailAlgebraConfig] = semiauto.deriveReader[EmailAlgebraConfig]
  override def default[F[_]: Sync]: F[EmailAlgebraConfig] = this.load("ticheck.email")

}
