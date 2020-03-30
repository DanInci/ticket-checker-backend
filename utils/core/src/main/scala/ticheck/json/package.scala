package ticheck

import busymachines.pureharm

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

}
