package ticheck.dao.ticket

import busymachines.pureharm.db.DAOAlgebra
import ticheck.TicketID
import ticheck.dao.ticket.models.TicketTable

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait TicketSQL[H[_]] extends DAOAlgebra[H, TicketTable, TicketID]
