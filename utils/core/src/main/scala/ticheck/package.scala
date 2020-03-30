import busymachines.pureharm
import io.chrisdavenport.fuuid

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object ticheck
    extends pureharm.PureharmCoreTypeDefinitions with pureharm.anomaly.AnomalyParamtersImplicits
    with pureharm.anomaly.PureharmAnomalyTypeDefinitions {

  final type FUUID = fuuid.FUUID
  final val FUUID: fuuid.FUUID.type = fuuid.FUUID

}
