package ticheck.auth

import ticheck.effect._
import ticheck.config._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
final case class JWTAuthConfig(
  expiration: JWTTokenExpirationDuration,
  secretKey:  SharedSecretKey,
)

object JWTAuthConfig extends ConfigLoader[JWTAuthConfig] {

  implicit protected val unsafeKeyReader: ConfigReader[SharedSecretKey] =
    ConfigReader[RawStringSharedSecretKey].map(SharedSecretKey.fromStringUnsafe)

  private case class JWTAuthConfigRepr(
    expiration: JWTTokenExpirationDuration,
    secretKey:  RawStringSharedSecretKey,
  )

  private object JWTAuthConfigRepr extends ConfigLoader[JWTAuthConfigRepr] {
    implicit override val configReader: ConfigReader[JWTAuthConfigRepr] = semiauto.deriveReader[JWTAuthConfigRepr]
    override def default[F[_]: Sync]: F[JWTAuthConfigRepr] = super.load("ticket-checker.auth.jwt")
  }

  override def configReader: ConfigReader[JWTAuthConfig] = semiauto.deriveReader[JWTAuthConfig]

  override def default[F[_]: Sync]: F[JWTAuthConfig] = {
    for {
      repr   <- JWTAuthConfigRepr.default[F]
      secret <- SharedSecretKey.fromString(repr.secretKey)
    } yield JWTAuthConfig(
      repr.expiration,
      secret,
    )
  }

}
