package ticheck.auth.models

import ticheck.auth.PlainTextPassword
import ticheck.{Email, Name}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class RegistrationRequest(
  email:    Email,
  password: PlainTextPassword,
  name:     Name,
)

object RegistrationRequest {
  import ticheck.json._

  implicit val jsonCodec: Codec[RegistrationRequest] = derive.codec[RegistrationRequest]

}
