package ticheck.email.impl

import cats.data.NonEmptyList
import ticheck.Email
import ticheck.email._
import ticheck.effect._
import ticheck.logger._
import ticheck.email.model.Attachment

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
final private[email] class EmailAlgebraImpl[F[_]](
  private val jmc: JavaMailClient[F],
)(
  implicit val F:       Async[F],
  implicit val shifter: BlockingShifter[F],
) extends EmailAlgebra[F] {

  private val logger: Logger[F] = Logger.getLogger[F]

  override def sendEmail(to: Email, title: EmailTitle, message: EmailMessage): F[Unit] = shifter.blockOn {
    jmc.sendEmail(title, message, List.empty)(to).onError {
      case e => logger.warn(e)(s"Failed to send email to: $to")
    }
  }

  override def sendEmailWithAttachments(
    to:          Email,
    title:       EmailTitle,
    message:     EmailMessage,
    attachments: NonEmptyList[Attachment[F]],
  ): F[Unit] = shifter.blockOn {
    jmc.sendEmail(title, message, attachments.toList)(to).onError {
      case e => logger.warn(e)(s"Failed to send email to: $to")
    }
  }

  override def sendEmails(tos: NonEmptyList[Email], title: EmailTitle, message: EmailMessage): F[Unit] = {
    tos.traverse_ { to =>
      shifter.blockOn {
        jmc.sendEmail(title, message, List.empty)(to).onError {
          case e => logger.warn(e)(s"Failed to send email to: $to")
        }
      }
    }
  }

  override def sendEmailsWithAttachments(
    tos:         NonEmptyList[Email],
    title:       EmailTitle,
    message:     EmailMessage,
    attachments: NonEmptyList[Attachment[F]],
  ): F[Unit] =
    tos.traverse_ { to =>
      shifter.blockOn {
        jmc
          .sendEmail(title, message, attachments.toList)(to)
          .onError {
            case e => logger.warn(e)(s"Failed to send email to: $to")
          }
      }
    }
}

private[email] object EmailAlgebraImpl {

  def async[F[_]: Async: BlockingShifter](config: EmailConfig): F[EmailAlgebra[F]] = {
    for {
      jmc <- new JavaMailClient[F](config).pure[F]
    } yield new EmailAlgebraImpl[F](jmc)
  }

}
