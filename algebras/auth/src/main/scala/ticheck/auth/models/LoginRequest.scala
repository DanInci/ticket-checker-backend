package ticheck.auth.models

import ticheck.Email
import ticheck.auth.PlainTextPassword

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class LoginRequest(
  email:    Email,
  password: PlainTextPassword,
)

object LoginRequest {
  import ticheck.json._

  implicit val jsonCodec: Codec[LoginRequest] = derive.codec[LoginRequest]

}
