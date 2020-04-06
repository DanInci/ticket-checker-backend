package ticheck.dao.ticket

import ticheck.db.DAOAlgebra
import ticheck.dao.ticket.models.TicketRecord

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait TicketSQL[H[_]] extends DAOAlgebra[H, TicketRecord, TicketPK]
