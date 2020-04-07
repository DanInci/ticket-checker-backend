package ticheck.dao.organization.membership.impl

import java.sql.Timestamp

import ticheck.dao.organization.membership._
import ticheck.db._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
private[impl] trait OrganizationMembershipComposites extends CoreComposites {

  protected val timeAlgebra: TimeAlgebra

  implicit val userRoleMeta: Meta[OrganizationRole] =
    Meta[String].imap(OrganizationRole.unsafe)(_.asString)

  implicit val joinedAtMeta: Meta[JoinedAt] =
    Meta[Timestamp].imap(t => JoinedAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

}
