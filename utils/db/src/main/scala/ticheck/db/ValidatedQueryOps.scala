package ticheck.db

import cats.data.Validated
import doobie.util.fragment.Fragment
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait ValidatedQueryOps {

  implicit def toQuery(validatedQuery: Validated[String, String]): Fragment = ???

}
