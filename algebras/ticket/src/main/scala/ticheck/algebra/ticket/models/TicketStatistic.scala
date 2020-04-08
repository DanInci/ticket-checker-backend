package ticheck.algebra.ticket.models

import ticheck.algebra.ticket.{StatisticsCount, StatisticsTimestamp}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class TicketStatistic(
  count: StatisticsCount,
  date:  StatisticsTimestamp,
)

object TicketStatistic {
  import ticheck.json._

  implicit val jsonCodec: Codec[TicketStatistic] = derive.codec[TicketStatistic]
}
