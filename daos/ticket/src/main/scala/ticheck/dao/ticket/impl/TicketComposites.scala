package ticheck.dao.ticket.impl

import java.sql.Timestamp

import ticheck.dao.ticket.{SoldAt, SoldToBirthday, ValidatedAt}
import ticheck.db.{CoreComposites, Meta}
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait TicketComposites extends CoreComposites {

  protected val timeAlgebra: TimeAlgebra

  implicit val soldToBirthdayMeta: Meta[SoldToBirthday] =
    Meta[Timestamp].imap(t => SoldToBirthday.spook(timeAlgebra.toLocalDate(t)))(timeAlgebra.toTimestamp)

  implicit val soldAtMeta: Meta[SoldAt] =
    Meta[Timestamp].imap(t => SoldAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

  implicit val validatedAtMeta: Meta[ValidatedAt] =
    Meta[Timestamp].imap(t => ValidatedAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

}
