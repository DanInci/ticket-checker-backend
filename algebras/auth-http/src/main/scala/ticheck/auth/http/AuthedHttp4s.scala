package ticheck.auth.http

import ticheck.effect._
import ticheck.auth._
import ticheck.json.Decoder

import org.http4s.dsl.Http4sDsl
import org.http4s._
import org.http4s.server.AuthMiddleware
import org.http4s.util.CaseInsensitiveString

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/1/2020
  *
  */
trait AuthedHttp4s[F[_], T <: AuthCtx, HT] {

  implicit def S: Sync[F]

  protected def convertContext(ctx: T): F[HT]

  def authMiddleware(config: JWTAuthConfig)(implicit decode: Decoder[T]): AuthMiddleware[F, HT] = {
    AuthMiddleware(verifyToken(config).andThen(s => convertContext(s)).attempt, onAuthFailure)
  }

  //---------------------- VERIFY ----------------------

  private def verifyToken(config: JWTAuthConfig)(implicit decode: Decoder[T]): Kleisli[F, Request[F], T] =
    Kleisli { req: Request[F] =>
      val optHeader = req.headers.get(AuthedHttp4s.`X-Auth-Token`)
      optHeader match {
        case None =>
          S.raiseError[T](MissingXAuthTokenHeaderAnomaly)
        case Some(header) =>
          JWTAuthToken.verifyAndParseRaw[F, T](config.secretKey)(header.value)
      }
    }

  //---------------------- FAILURE ----------------------

  private def onAuthFailure: AuthedRoutes[Throwable, F] = Kleisli { _: AuthedRequest[F, Throwable] =>
    val fdsl = Http4sDsl[F]
    import fdsl._
    OptionT.liftF(Unauthorized(AuthedHttp4s.wwwHeader))
  }

}

object AuthedHttp4s {

  private[auth] val `X-Auth-Token` = CaseInsensitiveString("X-AUTH-TOKEN")
  private val challenges: NonEmptyList[Challenge] = NonEmptyList.of(
    Challenge(
      scheme = "Bearer",
      realm  = "Go to .../v1/auth/signIn to get valid bearer token",
    ),
  )

  private val wwwHeader = headers.`WWW-Authenticate`(challenges)

}
