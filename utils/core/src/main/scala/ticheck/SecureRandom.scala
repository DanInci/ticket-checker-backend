package ticheck

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/9/2020
  *
  */
sealed trait SecureRandom[F[_]] {
  def randomString(alphabet: String)(n: Int): F[String]
}

object SecureRandom {

  import fs2.Stream
  import ticheck.effect._

  def apply[F[_]](implicit instance: SecureRandom[F]): SecureRandom[F] = instance

  def newSecureRandom[F[_]: Sync]: F[SecureRandom[F]] =
    for {
      jsec <- Sync[F].delay(new java.security.SecureRandom())
    } yield new SyncSecureRandom[F](jsec)

  private class SyncSecureRandom[F[_]](
    private val random:     java.security.SecureRandom,
  )(implicit private val F: Sync[F])
      extends SecureRandom[F] {

    def randomString(alphabet: String)(n: Int): F[String] = {
      Stream
        .eval(F.delay(random.nextInt(alphabet.length)).map(alphabet(_)))
        .repeat
        .take(n.toLong)
        .compile
        .toList
        .map(_.mkString)
    }
  }
}
