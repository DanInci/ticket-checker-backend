package ticheck

import busymachines.pureharm
import shapeless.tag.@@
import io.chrisdavenport.fuuid.circe._
import io.circe.Decoder.Result

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object json
    extends pureharm.json.PureharmJsonTypeDefinitions with pureharm.json.PureharmJsonImplicits
    with pureharm.internals.json.AnomalyJsonCodec {

  /**
    * This exists to give us the default behavior of deserializing sealed trait
    * hierarchies by adding and "_type" field to the json, instead of creating
    * a property for each variant.
    *
    * Unfortunately, this uses the name of each variant as the value for the
    * "_type" field, leaving JSON-value APIs vulnerable to rename refactorings.
    *
    */
  implicit final val defaultDerivationConfiguration: io.circe.generic.extras.Configuration =
    io.circe.generic.extras.Configuration.default
      .withDiscriminator(pureharm.json.DefaultTypeDiscriminatorConfig.JsonTypeString)

  implicit final def fuuidPhantomTypeDecoder[Tag]: Decoder[FUUID @@ Tag] =
    Decoder[FUUID].asInstanceOf[Decoder[FUUID @@ Tag]]

  implicit final def fuuidPhantomTypeEncoder[Tag]: Encoder[FUUID @@ Tag] =
    Encoder[FUUID].asInstanceOf[Encoder[FUUID @@ Tag]]

  implicit final def listCodec[A](implicit codec: Codec[A]): Codec[List[A]] = new Codec[List[A]] {
    override def apply(a: List[A]): Json            = Json.fromValues(a.map(codec.apply))
    override def apply(c: HCursor): Result[List[A]] = Decoder.decodeList(codec).apply(c)
  }

  object derive extends pureharm.json.SemiAutoDerivation

}
