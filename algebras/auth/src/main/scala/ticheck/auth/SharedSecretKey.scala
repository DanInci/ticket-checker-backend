package ticheck.auth

import java.nio.charset

import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
trait SharedSecretKey {
  def signingKey: SigningKey
}

object SharedSecretKey {

  def apply(key: SigningKey): SharedSecretKey = new SharedSecretKeyImpl(key)

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
    Sync[F].delay(SigningKey.spook(raw.map(_.toChar).mkString)).map(this.apply)

  /**
    * Recommended way of storing and transmitting shared secrets through
    * your application
    *
    * @param raw
    *   The shared secret represented as a byte encoded UTF-8 String
    */
  def fromRawBytesUnsafe[F[_]](raw: RawBytesSharedSecretKey): SharedSecretKey =
    this.apply(SigningKey.spook(raw.map(_.toChar).mkString))

  final private class SharedSecretKeyImpl private[SharedSecretKey] (
    private val key: SigningKey,
  ) extends SharedSecretKey { override val signingKey: SigningKey = key }
}
