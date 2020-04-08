package ticheck.organizer.user.models

import ticheck.algebra.user.models.UserProfile
import ticheck.auth.JWTAuthToken

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class LoginResponse(
  token:   JWTAuthToken,
  profile: UserProfile,
)

object LoginResponse {
  import ticheck.json._

  implicit val jsonCodec: Codec[LoginResponse] = derive.codec[LoginResponse]
}
