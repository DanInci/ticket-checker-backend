package ticheck.db

import ticheck.FUUID
import java.util.UUID

import shapeless.tag.@@

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait CoreComposites extends GenericComposites {

  implicit val fuuidMeta: Meta[FUUID] = Meta[UUID].imap(FUUID.fromUUID)(FUUID.Unsafe.toUUID)

  implicit def fuuidPhantomMeta[Tag]: Meta[FUUID @@ Tag] = fuuidMeta.asInstanceOf[Meta[FUUID @@ Tag]]

}

object CoreComposites extends CoreComposites
