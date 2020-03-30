package ticheck

import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait PhantomFUUID extends PhantomType[FUUID] {

  def generate[F[_]: Sync]: F[Type] = FUUID.randomFUUID.map(f => this.spook(f))
  def unsafeGenerate: Type = this.spook(FUUID.randomFUUID[IO].unsafeRunSync())

}
