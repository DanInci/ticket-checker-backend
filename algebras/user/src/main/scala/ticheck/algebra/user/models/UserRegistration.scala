package ticheck.algebra.user.models

import ticheck.{Email, Name}
import ticheck.algebra.user.PlainTextPassword

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserRegistration(
  email:    Email,
  password: PlainTextPassword,
  name:     Name,
)

object UserRegistration {
  import ticheck.json._

  implicit val jsonCodec: Codec[UserRegistration] = derive.codec[UserRegistration]

}
