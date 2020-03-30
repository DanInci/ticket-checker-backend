package ticheck.http

import ticheck.logger._
import ticheck._
import ticheck.effect._
import ticheck.json._

import fs2.Chunk
import io.circe.Printer
import org.http4s.{DecodeResult, EntityDecoder, EntityEncoder, MediaType}
import org.http4s.circe.CirceInstances
import org.http4s.headers.`Content-Type`

/**
  *
  * You need to have this in scope if you want "seamless" serializing/deserializing
  * to/from JSON in your HttpRoutes endpoints.
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait Http4sCirceInstances {

  import Http4sCirceInstances._

  /**
    * This code was copied from [[org.http4s.circe.CirceInstances#jsonEncoderWithPrinter]]
    * Ideally, we would have done directly:
    * {{{
    *   circeInstance.jsonEncoderOf[F, T]
    * }}}
    * But that throws us into an infinite loop because the implicit picks itself up.
    *
    * @return
    */
  implicit def applicativeEntityJsonEncoder[F[_]: Applicative, T: Encoder]: EntityEncoder[F, T] =
    EntityEncoder[F, Chunk[Byte]]
      .contramap[Json] { json =>
        val bytes = printer.printToByteBuffer(json)
        Chunk.byteBuffer(bytes)
      }
      .withContentType(`Content-Type`(MediaType.application.json))
      .contramap(t => Encoder.apply[T].apply(t))

  implicit def syncEntityJsonDecoder[F[_]: Sync, T: Decoder]: EntityDecoder[F, T] =
    circeInstances.jsonOf[F, T].handleErrorWith { mf =>
      val anomaly: AnomalyBase = mf.cause match {
        case Some(cdf: io.circe.DecodingFailure) => InvalidJSONBodyAnomaly(cdf)
        case Some(e) => KnownInvalidMessageAnomaly(mf.getMessage(), e)
        case None    => InvalidMessageAnomaly(mf.getMessage())
      }

      val anomalyJSON = anomaly.asJson.noSpaces
      val logger      = Logger.getLoggerFromName[F]("json.deserialization")
      DecodeResult.failure(
        logger.debug(s"Failed to deserialize json. Anomaly: $anomalyJSON") >> mf.pure[F],
      )
    }

}

object Http4sCirceInstances {
  private val printer:        Printer        = Printer.noSpaces.copy(dropNullValues = true)
  private val circeInstances: CirceInstances = CirceInstances.withPrinter(printer).build
}
