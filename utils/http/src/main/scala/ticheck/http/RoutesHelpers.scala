package ticheck.http

import java.time.LocalDateTime

import ticheck.{FUUID, PhantomType, _}
import io.chrisdavenport.fuuid.FUUID
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}
import shapeless.tag.@@
import ticheck.time.TimeFormatters
import ticheck.effect._

import scala.util.Try

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait RoutesHelpers {

  implicit def userIdQueryParamMatcher[F[_]]: QueryParamDecoder[UserID] =
    phantomTypeQueryParamDecoder[F, FUUID, UserID.Tag]

  implicit val pageOffsetQueryParamDecoder: QueryParamDecoder[PageNumber] =
    QueryParamDecoder.intQueryParamDecoder.map(PageNumber.apply)

  implicit val limitOffsetQueryParamDecoder: QueryParamDecoder[PageSize] =
    QueryParamDecoder.intQueryParamDecoder.map(PageSize.apply)

  implicit val localDateTimeQueryParamDecoder: QueryParamDecoder[LocalDateTime] =
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
