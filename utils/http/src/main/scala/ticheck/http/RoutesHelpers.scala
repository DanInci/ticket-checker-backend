package ticheck.http

import java.time.LocalDateTime

import ticheck.PhantomType
import io.chrisdavenport.fuuid.FUUID
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}
import shapeless.tag.@@
import ticheck.time.TimeFormatters
import ticheck.effect._

import scala.util.{Failure, Try}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait RoutesHelpers {

  implicit val localDateTimeQueryParamMatcher: QueryParamDecoder[LocalDateTime] =
    (value: QueryParameterValue) => {
      Try {
        LocalDateTime.parse(value.value, TimeFormatters.dateTimeFormatter)
      }.toEither.leftMap(t => ParseFailure("Query param decoding failed", t.getMessage)).toValidatedNel,
    }

  implicit val fuuidDecoder: QueryParamDecoder[FUUID] = io.chrisdavenport.fuuid.http4s.implicits.fuuidQueryParamDecoder

  implicit def phantomTypeQueryParamDecoder[F[_], P, T <: PhantomType[P]](
    implicit p: QueryParamDecoder[P],
  ): QueryParamDecoder[P @@ T] =
    (value: QueryParameterValue) => p.decode(value).map(shapeless.tag[T](_))

}
