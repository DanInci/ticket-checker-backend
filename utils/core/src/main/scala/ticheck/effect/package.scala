package ticheck

import busymachines.pureharm.effects

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object effect extends effects.PureharmEffectsAllTypes with effects.PureharmEffectsAllImplicits {
  import shapeless.tag.@@

  implicit final def phantomFUUIDShow[Tag](implicit sh: Show[FUUID]): Show[FUUID @@ Tag] =
    sh.asInstanceOf[Show[FUUID @@ Tag]]

}
