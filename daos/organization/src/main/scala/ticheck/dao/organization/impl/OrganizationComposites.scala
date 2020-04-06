package ticheck.dao.organization.impl

import java.sql.Timestamp

import ticheck.CreatedAt
import ticheck.db.{CoreComposites, Meta}
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
private[impl] trait OrganizationComposites extends CoreComposites {

  protected val timeAlgebra: TimeAlgebra

  implicit val createdAtMeta: Meta[CreatedAt] =
    Meta[Timestamp].imap(t => CreatedAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

}
