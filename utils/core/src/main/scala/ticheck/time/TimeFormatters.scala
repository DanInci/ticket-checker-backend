package ticheck.time

import java.time.format.DateTimeFormatter

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
object TimeFormatters {
  lazy val dateFormatter:     DateTimeFormatter = DateTimeFormatter.ISO_DATE
  lazy val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
}
