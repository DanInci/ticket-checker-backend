package ticheck.algebra.user.models

import ticheck.dao.user.models.UserRecord
import ticheck.{CreatedAt, Email, Name, UserID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserProfile(
  id:        UserID,
  email:     Email,
  name:      Name,
  createdAt: CreatedAt,
)

object UserProfile {
  import ticheck.json._

  implicit val jsonCodec: Codec[UserProfile] = derive.codec[UserProfile]

  def fromDAO(u: UserRecord): UserProfile =
    UserProfile(
      u.id,
      u.email,
      u.name,
      u.createdAt,
    )

}
