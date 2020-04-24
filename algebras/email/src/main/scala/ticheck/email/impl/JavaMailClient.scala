package ticheck.email.impl

import ticheck.Email
import ticheck.effect._
import ticheck.logger._
import ticheck.email._
import java.util.Properties

import javax.mail.{Session, Transport}
import javax.mail.internet.MimeMessage
import ticheck.email.model.Attachment

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
private[impl] class JavaMailClient[F[_]](
  private val config: EmailConfig,
)(
  implicit private val F: Sync[F],
) {

  implicit private val logger: Logger[F] = Logger.getLogger[F]

  def sendEmail(title: EmailTitle, emailMessage: EmailMessage, atts: List[Attachment[F]])(to: Email): F[Unit] = {
    val resource = for {
      session   <- sessionResource(properties)
      transport <- transportResource(session)
    } yield (session, transport)

    resource.use { t: (Session, Transport) =>
      val (session, transport) = t
      for {
        mime <- JavaMailMime.createMimeMessage(session, config.emailFrom)(to, title, emailMessage, atts)
        _    <- sendMimeMessage(transport)(mime)
      } yield ()
    }

  }

  private def sendMimeMessage(transport: Transport)(mime: MimeMessage): F[Unit] = {
    for {
      _ <- F
        .delay(transport.sendMessage(mime, mime.getAllRecipients))
        .onError { case e => logger.warn(e)(s"Failed to send email") }
    } yield ()
  }

  def sessionResource(properties: Properties): Resource[F, Session] = {
    Resource.pure(Session.getDefaultInstance(properties))(F)
  }

  def transportResource(session: Session): Resource[F, Transport] = {
    val makeF = F.delay {
      val transport = session.getTransport()
      transport.connect(config.smtpHostName, config.smtpUsername, config.smtpPassword)
      transport
    }
    val releaseF = (t: Transport) => F.delay(t.close())

    Resource.make(makeF)(releaseF)
  }

  /**
    * A complete list of session properties can be found at
    * https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html
    *
    * session is just a data structure. It can be reused every time.
    */
  private[this] lazy val properties: Properties = {
    val props: Properties = System.getProperties
    props.setProperty("mail.smtp.from", config.emailFrom)
    props.put("mail.smtp.host", config.smtpHostName)
    props.put("mail.smtp.port", config.smtpPort.toString)
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtps.auth", "true")
    props.put("mail.transport.protocol", "smtps")
    props
  }

}
