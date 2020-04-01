package ticheck.auth

import cats.Show

import ticheck.effect._
import ticheck.json._

import tsec.jwt._
import tsec.jws.mac._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
sealed trait JWTAuthToken {
  def rawToken: String

  final override def toString: String = s"JWTAuthToken($rawToken)"
}

object JWTAuthToken {

  private val AuthCtxCustomClaimField = "authCtx"

  /**
    * Overload of [[create(SharedSecretKey, JWTTokenExpirationDuration)]]
    */
  def create[F[_]: Sync, T <: AuthCtx](
    config:  JWTAuthConfig,
  )(authCtx: T)(implicit encode: Encoder[T]): F[JWTAuthToken] =
    this.create(config.secretKey, config.expiration)(authCtx)

  /**
    * We create a JWT signed with the given shared secret key.
    * And a custom ``authCtx`` claim which contains the information
    * about the user given in [[AuthCtx]]
    */
  def create[F[_]: Sync, T <: AuthCtx](key: SharedSecretKey, expiresAfter: JWTTokenExpirationDuration)(
    authCtx:         T,
  )(implicit encode: Encoder[T]): F[JWTAuthToken] = {
    for {
      claims <- JWTClaims.withDuration(
        expiration   = Option(expiresAfter),
        customFields = Seq((AuthCtxCustomClaimField, encode(authCtx))),
      )
      jwtStr <- JWTMac.buildToString[F, key.SigningAlg](claims, key.signingKey)
    } yield JWTAuthTokenImpl(jwtStr)
  }

  /**
    * The inverse of [[create]]
    */
  def verifyAndParse[F[_]: Sync, T <: AuthCtx](
    key: SharedSecretKey,
  )(jwt: JWTAuthToken)(implicit decode: Decoder[T]): F[T] =
    verifyAndParseRaw(key)(jwt.rawToken)

  /**
    * The inverse of [[create]]
    */
  def verifyAndParseRaw[F[_]: Sync, T <: AuthCtx](
    key: SharedSecretKey,
  )(jwt: String)(implicit decode: Decoder[T]): F[T] = {
    for {
      jwt <- JWTMac.verifyAndParse[F, key.SigningAlg](jwt, key.signingKey).adaptError {
        case NonFatal(_) => JWTVerificationAnomaly
      }
      ctx <- jwt.body.getCustomF[F, T](AuthCtxCustomClaimField).adaptError {
        case NonFatal(e) => JWTAuthCtxMalformedAnomaly(e.getMessage)
      }
    } yield ctx
  }

  final private case class JWTAuthTokenImpl(override val rawToken: String) extends JWTAuthToken

  implicit val JWTAuthTokenShow: Show[JWTAuthToken] = (t: JWTAuthToken) => t.toString

  private val JWTAUthTokenEncoder: Encoder[JWTAuthToken] = Encoder.instance(t => Json.fromString(t.rawToken))
  private val JWTAUthTokenDecoder: Decoder[JWTAuthToken] = Decoder.decodeString.map(s => JWTAuthTokenImpl(s))
  implicit val JWTAuthTokenCodec:  Codec[JWTAuthToken]   = Codec.from(JWTAUthTokenDecoder, JWTAUthTokenEncoder)

}
