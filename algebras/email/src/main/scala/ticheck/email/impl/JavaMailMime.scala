package ticheck.email.impl

import java.net.URLConnection

import ticheck._
import ticheck.effect._
import io.chrisdavenport.fuuid.FUUID
import javax.activation.DataHandler
import javax.mail.{Message, Session}
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}
import javax.mail.util.ByteArrayDataSource
import ticheck.email._
import ticheck.email.model.Attachment

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
private[impl] object JavaMailMime {

  def createMimeMessage[F[_]: Sync](
    session: Session,
    from:    Email,
  )(
    to:           Email,
    title:        EmailTitle,
    emailMessage: EmailMessage,
    atts:         List[Attachment[F]],
  ): F[MimeMessage] = {
    for {
      mm <- mimeHeaders(session)(
        to    = to,
        from  = from,
        title = title,
      )
      mmAtts <- mimeBody(mm, emailMessage, atts)
      _      <- Sync[F].delay(mmAtts.saveChanges())
    } yield mmAtts

  }

  private def mimeHeaders[F[_]: Sync](
    session: Session,
  )(
    to:    Email,
    from:  Email,
    title: EmailTitle,
  ): F[MimeMessage] = Sync[F].pure {

    val message: MimeMessage = new MimeMessage(session)

    message.setFrom(new InternetAddress(from))
    message.setRecipient(Message.RecipientType.TO, new InternetAddress(to))
    message.setSubject(title)

    message
  }

  private def mimeBody[F[_]: Sync](
    mime:         MimeMessage,
    emailMessage: EmailMessage,
    atts:         List[Attachment[F]],
  ): F[MimeMessage] = {

    def formatBody(body: EmailMessage): EmailMessage = {
      val links = """(\s)(https?://[^\s]+)(\s)""".r
      EmailMessage(links.replaceAllIn(body, "$1<a href=\"$2\">$2</a>$3").replace("\n", "<br/>"))
    }

    def bodyText(body: EmailMessage): MimeMultipart = {
      val wrap    = new MimeBodyPart
      val cover   = new MimeMultipart("alternative")
      val html    = new MimeBodyPart
      val content = new MimeMultipart("mixed")

      cover.addBodyPart(html)
      wrap.setContent(cover)
      content.addBodyPart(wrap)
      html.setContent(s"<html><body>\n${formatBody(body)}</body></html>", "text/html; charset=UTF-8")
      content
    }

    for {
      mimeMultipart <- bodyText(emailMessage).pure[F]
      withAtts      <- addAttachements(mimeMultipart, atts)
      _ = mime.setContent(withAtts)
    } yield mime
  }

  private def addAttachements[F[_]: Sync](content: MimeMultipart, atts: List[Attachment[F]]): F[MimeMultipart] = {
    for {
      all <- atts.traverse(attachmentAsMimeBodyPart[F])
      _   <- Sync[F].delay(all.foreach(att => content.addBodyPart(att)))
    } yield content
  }

  private lazy val `Content-ID` = "CONTENT-ID"

  private def attachmentAsMimeBodyPart[F[_]: Sync](a: Attachment[F]): F[MimeBodyPart] = {
    for {
      mimeType <- mimeTypeFromFileName(a.name)
      bytes    <- a.content.compile.toVector
      dataS <- Sync[F].delay {
        val newDS = new ByteArrayDataSource(bytes.toArray, mimeType)
        newDS.setName(a.name)
        newDS
      }
      fuuid <- FUUID.randomFUUID[F]
      mimeBP <- Sync[F].delay {
        val bp = new MimeBodyPart
        bp.setDataHandler(new DataHandler(dataS))
        bp.setHeader(`Content-ID`, s"<${fuuid.show}>")
        bp.setFileName(a.name)
        bp
      }
    } yield mimeBP
  }

  private def mimeTypeFromFileName[F[_]: Sync](an: AttachmentName): F[String] = {
    import java.nio.file.Paths
    for {
      jPath    <- Sync[F].catchNonFatal(Paths.get(AttachmentName.despook(an)))
      mimeType <- Sync[F].delay(URLConnection.guessContentTypeFromName(jPath.getFileName.toString))
    } yield mimeType
  }

}
