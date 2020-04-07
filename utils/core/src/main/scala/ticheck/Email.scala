package ticheck

import ticheck.effect._
import ticheck.json._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
object Email extends SafePhantomType[Throwable, String] {

  private val emailDecoder: Decoder[Email.Type] = Decoder[String].emap(es => Email(es).leftMap(_.getMessage))
  private val emailEncoder: Encoder[Email.Type] = Encoder[String].contramap(Email.despook)

  implicit val EmailCodec: Codec[Email.Type] = Codec.from[Email.Type](emailDecoder, emailEncoder)

  override def check(value: String): Either[Throwable, String] = {
    val normalized = normalize(value)
    if (!isValid(normalized))
      Either.left(InvalidEmailAnomaly(value))
    else
      Either.right(normalized)
  }

  private def normalize(em: String): String = em.toLowerCase.trim

  private def isValid(em: String): Boolean =
    em.matches(
      "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+" +
        "/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
        "\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
        "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
        "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
    )

}
