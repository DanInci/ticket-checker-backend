package ticheck.algebra.user.models

import ticheck.Email
import ticheck.algebra.user.PlainTextPassword

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserLoginRequest(
  email:    Email,
  password: PlainTextPassword,
)

object UserLoginRequest {
  import ticheck.json._

  implicit val jsonCodec: Codec[UserLoginRequest] = derive.codec[UserLoginRequest]

}
