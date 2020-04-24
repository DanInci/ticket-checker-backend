package ticheck.email

import ticheck.Email
import ticheck.effect._
import ticheck.email.model.Attachment

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
trait EmailAlgebra[F[_]] {

  def sendEmail(to: Email, title: EmailTitle, message: EmailMessage): F[Unit]

  def sendEmailWithAttachments(
    to:          Email,
    title:       EmailTitle,
    message:     EmailMessage,
    attachments: NonEmptyList[Attachment[F]],
  ): F[Unit]

  def sendEmails(to: NonEmptyList[Email], title: EmailTitle, message: EmailMessage): F[Unit]

  def sendEmailsWithAttachments(
    to:          NonEmptyList[Email],
    title:       EmailTitle,
    message:     EmailMessage,
    attachments: NonEmptyList[Attachment[F]],
  ): F[Unit]

}
