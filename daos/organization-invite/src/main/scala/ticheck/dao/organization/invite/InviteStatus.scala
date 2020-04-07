package ticheck.dao.organization.invite

import ticheck.effect._
import ticheck.json._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
sealed trait InviteStatus {
  def asString: String
}

object InviteStatus {

  implicit def userRoleCodec: Codec[InviteStatus] = Codec.from(userRoleDecoder, userRoleEncoder)

  private val userRoleEncoder: Encoder[InviteStatus] = Encoder.apply[String].contramap(_.asString)
  private val userRoleDecoder: Decoder[InviteStatus] =
    Decoder.apply[String].emap(s => InviteStatus.fromString(s).leftMap(_.getMessage))

  private val PendingStr  = "PENDING"
  private val AcceptedStr = "ACCEPTED"
  private val DeclinedStr = "DECLINED"

  final object InvitePending extends InviteStatus {
    override def asString: String = PendingStr
  }
  final object InviteAccepted extends InviteStatus {
    override def asString: String = AcceptedStr
  }
  final object InviteDeclined extends InviteStatus {
    override def asString: String = DeclinedStr
  }

  private val rolesMap: Map[String, InviteStatus] =
    Map(
      PendingStr  -> InvitePending,
      AcceptedStr -> InviteAccepted,
      DeclinedStr -> InviteDeclined,
    )

  def fromString(roleString: String): Attempt[InviteStatus] = rolesMap.get(roleString) match {
    case None    => Attempt.raiseError(InvalidInviteStatusAnomaly(roleString))
    case Some(r) => Attempt.pure(r)
  }

  def unsafe(s: String): InviteStatus = this.fromString(s) match {
    case Left(e)      => throw e
    case Right(value) => value
  }

}
