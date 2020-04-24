package ticheck.email.model

import fs2.Stream
import ticheck.email.AttachmentName

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
final case class Attachment[F[_]](
  name:    AttachmentName,
  content: Stream[F, Byte],
)
