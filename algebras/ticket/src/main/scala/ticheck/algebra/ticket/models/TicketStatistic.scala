package ticheck.algebra.ticket.models

import ticheck.Count
import ticheck.dao.ticket.{EndDate, StartDate}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class TicketStatistic(
  count:     Count,
  startDate: StartDate,
  endDate:   EndDate,
)

object TicketStatistic {
  import ticheck.json._

  implicit val jsonCodec: Codec[TicketStatistic] = derive.codec[TicketStatistic]
}
