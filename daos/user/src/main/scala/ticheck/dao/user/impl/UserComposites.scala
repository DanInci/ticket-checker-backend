package ticheck.dao.user.impl

import java.sql.Timestamp

import ticheck.CreatedAt
import ticheck.dao.user.{EditedAt, _}
import ticheck.db._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
private[impl] trait UserComposites extends CoreComposites {

  protected val timeAlgebra: TimeAlgebra

  implicit val emailMeta: Meta[Email] =
    Meta[String].imap(Email.unsafe)(Email.despook)

  implicit val userRoleMeta: Meta[UserRole] =
    Meta[String].imap(UserRole.unsafe)(_.asString)

  implicit val createdAtMeta: Meta[CreatedAt] =
    Meta[Timestamp].imap(t => CreatedAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

  implicit val editedAt: Meta[EditedAt] =
    Meta[Timestamp].imap(t => EditedAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

}
