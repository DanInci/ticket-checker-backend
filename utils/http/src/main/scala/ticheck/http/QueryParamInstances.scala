package ticheck.http

import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher
import ticheck.{PageNumber, PageSize}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
trait QueryParamInstances {

  protected object PageNumberMatcher extends OptionalQueryParamDecoderMatcher[PageNumber]("page")
  protected object PageSizeMatcher   extends OptionalQueryParamDecoderMatcher[PageSize]("pageSize")

}
