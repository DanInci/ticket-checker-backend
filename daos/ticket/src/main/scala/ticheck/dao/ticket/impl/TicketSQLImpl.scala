package ticheck.dao.ticket.impl

import ticheck.dao.ticket.{TicketPK, TicketSQL}
import ticheck.dao.ticket.models.TicketRecord
import ticheck.db._
import ticheck.effect._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
final private[ticket] class TicketSQLImpl private (override val timeAlgebra: TimeAlgebra)
    extends TicketSQL[ConnectionIO] with TicketComposites {

  override def find(pk: TicketPK): ConnectionIO[Option[TicketRecord]] =
    sql"""SELECT "id", "organization_id", "sold_to", "sold_to_birthday", "sold_to_telephone", "sold_by_id", "sold_by_name", "sold_at", "validated_by_id", "validated_by_name", "validated_at"
         | FROM "ticket"
         | WHERE "id"=${pk._1} AND "organization_id"=${pk._2}""".stripMargin.query[TicketRecord].option

  override def retrieve(pk: TicketPK)(implicit show: Show[TicketPK]): ConnectionIO[TicketRecord] =
    sql"""SELECT "id", "organization_id", "sold_to", "sold_to_birthday", "sold_to_telephone", "sold_by_id", "sold_by_name", "sold_at", "validated_by_id", "validated_by_name", "validated_at"
         | FROM "ticket"
         | WHERE "id"=${pk._1} AND "organization_id"=${pk._2}""".stripMargin.query[TicketRecord].unique

  override def insert(e: TicketRecord): ConnectionIO[TicketPK] =
    sql"""INSERT INTO "ticket" ("id", "organization_id", "sold_to", "sold_to_birthday", "sold_to_telephone", "sold_by_id", "sold_by_name", "sold_at", "validated_by_id", "validated_by_name", "validated_at")
         | VALUES (${e.id}, ${e.organizationId}, ${e.soldTo}, ${e.soldToBirthday}, ${e.soldToTelephone}, ${e.soldBy}, ${e.soldByName}, ${e.soldAt}, ${e.validatedBy}, ${e.validatedByName}, ${e.validatedAt})""".stripMargin.update
      .withUniqueGeneratedKeys[TicketPK]("id", "organization_id")

  override def insertMany(es: Iterable[TicketRecord]): ConnectionIO[Unit] = ???

  override def update(e: TicketRecord): ConnectionIO[TicketRecord] =
    sql"""UPDATE "ticket" 
         | SET "sold_to"=${e.soldTo}, "sold_to_birthday"=${e.soldToBirthday}, "sold_to_telephone"=${e.soldToTelephone}, "sold_by_id"=${e.soldBy}, "sold_by_name"=${e.soldByName}, "sold_at"=${e.soldAt}, "validated_by_id"=${e.validatedBy}, "validated_by_name"=${e.validatedByName}, "validated_at"=${e.validatedAt}
         | WHERE "id"=${e.id} AND "organization_id"=${e.organizationId}""".stripMargin.update
      .withUniqueGeneratedKeys[TicketRecord](
        "id",
        "organization_id",
        "sold_to",
        "sold_to_birthday",
        "sold_to_telephone",
        "sold_by_id",
        "sold_by_name",
        "sold_at",
        "validated_by_id",
        "validated_by_name",
        "validated_at",
      )

  override def updateMany[M[_]](es: M[TicketRecord])(implicit evidence$1: Traverse[M]): ConnectionIO[Unit] = ???

  override def delete(pk: TicketPK): ConnectionIO[Unit] =
    sql"""DELETE FROM "ticket" WHERE "id"=${pk._1} AND "organization_id"=${pk._2}""".stripMargin.update.run.void

  override def deleteMany(pks: Iterable[TicketPK]): ConnectionIO[Unit] = ???

  override def exists(pk: TicketPK): ConnectionIO[Boolean] = find(pk).map(_.isDefined)

  override def existsAtLeastOne(pks: Iterable[TicketPK]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[TicketPK]): ConnectionIO[Boolean] = ???

}

private[ticket] object TicketSQLImpl {

  def sync[F[_]](timeAlgebra: TimeAlgebra)(implicit F: Sync[F]): F[TicketSQL[ConnectionIO]] =
    F.pure(new TicketSQLImpl(timeAlgebra))

}
