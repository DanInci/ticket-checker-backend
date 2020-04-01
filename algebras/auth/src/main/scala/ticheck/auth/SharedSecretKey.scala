package ticheck.auth

import java.nio.charset

import ticheck.effect._
import tsec.mac.jca._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
trait SharedSecretKey {
  final type SigningAlg = SharedSecretKey.Algo
  final type SigningKey = MacSigningKey[SigningAlg]

  def signingKey: SigningKey
}

object SharedSecretKey {

  //convenience, for easy upgrading later on
  private type Algo = HMACSHA384
  private val Algo = HMACSHA384

  def apply(key: MacSigningKey[Algo]): SharedSecretKey = new SharedSecretKeyImpl(key)

  def generateKey[F[_]: Sync]: F[SharedSecretKey] = Algo.generateKey[F].map(this.apply)

  /**
    *
    * Please prefer using the [[fromRawBytes]] method, and do not store shared
    * secrets in a String
    *
    * @param raw
    *   Shared secret as a UTF-8 encoded String
    */
  def fromString[F[_]: Sync](raw: RawStringSharedSecretKey): F[SharedSecretKey] =
    this.fromRawBytes(RawBytesSharedSecretKey(raw.getBytes(charset.StandardCharsets.UTF_8)))

  /**
    *
    * Please prefer using the [[fromRawBytes]] method, and do not store shared
    * secrets in a String
    *
    * @param raw
    *   Shared secret as a UTF-8 encoded String
    */
  def fromStringUnsafe(raw: RawStringSharedSecretKey): SharedSecretKey =
    this.fromRawBytesUnsafe(RawBytesSharedSecretKey(raw.getBytes(charset.StandardCharsets.UTF_8)))

  /**
    * Recommended way of storing and transmitting shared secrets through
    * your application
    *
    * @param raw
    *   The shared secret represented as a byte encoded UTF-8 String
    */
  def fromRawBytes[F[_]: Sync](raw: RawBytesSharedSecretKey): F[SharedSecretKey] =
    Algo.buildKey[F](raw).map(this.apply)

  /**
    * Recommended way of storing and transmitting shared secrets through
    * your application
    *
    * @param raw
    *   The shared secret represented as a byte encoded UTF-8 String
    */
  def fromRawBytesUnsafe[F[_]](raw: RawBytesSharedSecretKey): SharedSecretKey =
    this.apply(Algo.unsafeBuildKey(raw))

  final private class SharedSecretKeyImpl private[SharedSecretKey] (
    private val key: MacSigningKey[Algo],
  ) extends SharedSecretKey { override val signingKey: SigningKey = key }
}
