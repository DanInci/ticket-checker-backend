package ticheck.dao.organization.invite.impl

import java.sql.Timestamp

import ticheck.Email
import ticheck.dao.organization.invite.{AnsweredAt, _}
import ticheck.db.{CoreComposites, Meta}
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
private[impl] trait OrganizationInviteComposites extends CoreComposites {

  protected val timeAlgebra: TimeAlgebra

  implicit val inviteStatusMeta: Meta[InviteStatus] =
    Meta[String].imap(InviteStatus.unsafe)(_.asString)

  implicit val emailMeta: Meta[Email] =
    Meta[String].imap(Email.unsafe)(Email.despook)

  implicit val invitedAtMeta: Meta[InvitedAt] =
    Meta[Timestamp].imap(t => InvitedAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

  implicit val answeredAtMeta: Meta[AnsweredAt] =
    Meta[Timestamp].imap(t => AnsweredAt.spook(timeAlgebra.toOffsetDateTime(t)))(timeAlgebra.toTimestamp)

}
