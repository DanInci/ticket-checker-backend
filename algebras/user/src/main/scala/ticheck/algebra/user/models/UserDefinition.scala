package ticheck.algebra.user.models

import ticheck.Name

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class UserDefinition(
  name: Name,
)

object UserDefinition {
  import ticheck.json._

  implicit val jsonCodec: Codec[UserDefinition] = derive.codec[UserDefinition]

}
