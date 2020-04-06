package ticheck.dao.ticket.impl

import ticheck.TicketID
import ticheck.dao.ticket.TicketSQL
import ticheck.dao.ticket.models.TicketTable
import ticheck.db._
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
private[ticket] object TicketSQLImpl extends TicketSQL[ConnectionIO] with TicketComposites {

  override def find(pk: TicketID): ConnectionIO[Option[TicketTable]] = ???

  override def retrieve(pk: TicketID)(implicit show: Show[TicketID]): ConnectionIO[TicketTable] = ???

  override def insert(e: TicketTable): ConnectionIO[TicketID] = ???

  override def insertMany(es: Iterable[TicketTable]): ConnectionIO[Unit] = ???

  override def update(e: TicketTable): ConnectionIO[TicketTable] = ???

  override def updateMany[M[_]](es: M[TicketTable])(implicit evidence$1: Traverse[M]): ConnectionIO[Unit] = ???

  override def delete(pk: TicketID): ConnectionIO[Unit] = ???

  override def deleteMany(pks: Iterable[TicketID]): ConnectionIO[Unit] = ???

  override def exists(pk: TicketID): ConnectionIO[Boolean] = ???

  override def existsAtLeastOne(pks: Iterable[TicketID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[TicketID]): ConnectionIO[Boolean] = ???

}
