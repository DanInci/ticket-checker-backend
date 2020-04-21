package ticheck.dao.ticket.impl

import doobie.util.fragment.Fragment
import ticheck.dao.ticket.TicketCategory.{SoldTicket, ValidatedTicket}
import ticheck.{Limit, Offset, OrganizationID, UserID}
import ticheck.dao.ticket.{Count, EndDate, StartDate, TicketCategory, TicketPK, TicketSQL}
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

  override def countTicketsBetweenDates(
    byCategory: TicketCategory,
    startDate:  StartDate,
    endDate:    EndDate,
  ): ConnectionIO[Count] = {
    val whereClause = byCategory match {
      case SoldTicket      => s"""WHERE "sold_at" >= '$startDate' AND "sold_at" < '$endDate'"""
      case ValidatedTicket => s"""WHERE "validated_at" >= '$startDate' AND "validated_at" < '$endDate'"""
    }

    (sql"""SELECT COUNT(*) FROM "ticket" """.stripMargin ++ Fragment.const(whereClause)).query[Count].unique
  }

  override def getAllForOrganization(
    organizationId: OrganizationID,
    offset:         Offset,
    limit:          Limit,
    byCategory:     Option[TicketCategory],
    byUserId:       Option[UserID],
    searchVal:      Option[String],
  ): ConnectionIO[List[TicketRecord]] = {
    val organizationIdWC = Some(s""""organization_id"='$organizationId'""")
    val byCategoryWC = byCategory.map {
      case SoldTicket =>
        s""""sold_at" IS NOT NULL"""
      case ValidatedTicket =>
        s""""validated_at" IS NOT NULL"""
      case _ => ""
    }
    val byUserIdWC = byUserId.map(
      uid =>
        byCategory match {
          case Some(SoldTicket)      => s""""sold_by_id"='$uid'"""
          case Some(ValidatedTicket) => s""""validated_by_id"='$uid'"""
          case _                     => s"""("sold_by_id"='$uid' OR "validated_by_id"='$uid')"""
        },
    )
    val searchValWC = searchVal.map(
      s =>
        byUserId match {
          case Some(_) => s""""sold_to" LIKE '$s%'"""
          case None    => s"""("sold_to" LIKE '$s%' OR "sold_by_name" LIKE '$s%' OR "validated_by_name" LIKE '$s%')"""
        },
    )
    val WCs         = List(organizationIdWC, byCategoryWC, byUserIdWC, searchValWC).flatten
    val whereClause = WCs.mkString("WHERE ", " AND ", "")

    (sql"""SELECT "id", "organization_id", "sold_to", "sold_to_birthday", "sold_to_telephone", "sold_by_id", "sold_by_name", "sold_at", "validated_by_id", "validated_by_name", "validated_at"
          | FROM "ticket" """.stripMargin
      ++ Fragment.const(whereClause) ++
      sql""" ORDER BY "sold_at" DESC OFFSET $offset LIMIT $limit """.stripMargin).query[TicketRecord].to[List]
  }

  override def findByUserID(userId: UserID, category: Option[TicketCategory]): ConnectionIO[List[TicketRecord]] = {
    val whereClause = category match {
      case None                                 => s"""WHERE "sold_by_id"='$userId' OR "validated_by_id"='$userId'"""
      case Some(TicketCategory.SoldTicket)      => s"""WHERE "sold_by_id"='$userId'"""
      case Some(TicketCategory.ValidatedTicket) => s"""WHERE "validated_by_id"='$userId'"""
    }

    (sql"""SELECT "id", "organization_id", "sold_to", "sold_to_birthday", "sold_to_telephone", "sold_by_id", "sold_by_name", "sold_at", "validated_by_id", "validated_by_name", "validated_at"
          | FROM "ticket" """.stripMargin ++ Fragment.const(whereClause)).query[TicketRecord].to[List]
  }

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

  override def updateMany[M[_]](es: M[TicketRecord])(implicit evidence$1: Traverse[M]): ConnectionIO[Unit] =
    es.traverse(update).void

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
