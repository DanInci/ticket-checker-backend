package ticheck

import io.chrisdavenport.log4cats.StructuredLogger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object logger {

  type Logger[F[_]] = StructuredLogger[F]
  val Logger: Slf4jLogger.type = Slf4jLogger

}
