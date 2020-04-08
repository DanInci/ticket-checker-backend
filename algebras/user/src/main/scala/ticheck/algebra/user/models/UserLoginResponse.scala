package ticheck.algebra.user.models

import ticheck.auth.JWTAuthToken

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserLoginResponse(
  token:   JWTAuthToken,
  profile: UserProfile,
)

object UserLoginResponse {
  import ticheck.json._

  implicit val jsonCodec: Codec[UserLoginResponse] = derive.codec[UserLoginResponse]

}
