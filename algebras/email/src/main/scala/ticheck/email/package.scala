package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
package object email {

  object AttachmentName extends PhantomType[String]
  type AttachmentName = AttachmentName.Type

  object EmailTitle extends PhantomType[String]
  type EmailTitle = EmailTitle.Type

  object EmailMessage extends PhantomType[String]
  type EmailMessage = EmailMessage.Type

  object SMTPUsername extends PhantomType[String]
  type SMTPUsername = SMTPUsername.Type

  object SMTPPlainTextPassword extends PhantomType[String]
  type SMTPPlainTextPassword = SMTPPlainTextPassword.Type

  object SMTPHostName extends PhantomType[String]
  type SMTPHostName = SMTPHostName.Type

  object SMTPPort extends PhantomType[Long]
  type SMTPPort = SMTPPort.Type

}
