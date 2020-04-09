package ticheck.algebra.user

import ticheck._
import ticheck.UserID

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/9/2020
  *
  */
final case class UserNFA private (userId: Option[UserID], email: Option[Email])
    extends NotFoundAnomaly(
      s"User with ${if (userId.isDefined) "id" else "email"} '${if (userId.isDefined) userId.get.show else email}' was not found",
    ) {

  def this(email: Email) { this(None, Some(email)) }

  def this(userId: UserID) { this(Some(userId), None) }

  override val id: AnomalyID = AnomalyIDs.UserNotFoundID
  override val parameters: Anomaly.Parameters = (userId, email) match {
    case (Some(uid), None) =>
      Anomaly.Parameters(
        "userId" -> uid.show,
      )
    case (None, Some(e)) =>
      Anomaly.Parameters(
        "email" -> e,
      )
    case _ => Anomaly.Parameters()
  }
}

final case class UserIsOrganizationOwnerCA(userId: UserID)
    extends ConflictAnomaly(s"User with id '$userId' is owner of some organizations. Can not be deleted") {

  override val id: AnomalyID = AnomalyIDs.UserIsOrganizationOwnerID
  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "userId" -> userId.show,
  )

}
