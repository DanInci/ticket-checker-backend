package ticheck.dao.organization.impl

import doobie.util.fragment.Fragment
import ticheck._
import ticheck.db._
import ticheck.effect._
import ticheck.dao.organization.{OrganizationName, OrganizationSQL}
import ticheck.dao.organization.models.OrganizationRecord
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
final private[organization] class OrganizationSQLImpl private (override val timeAlgebra: TimeAlgebra)
    extends OrganizationSQL[ConnectionIO] with OrganizationComposites {

  override def getAll(
    filterNot: Option[List[OrganizationID]],
    offset:    Offset,
    limit:     Limit,
  ): ConnectionIO[List[OrganizationRecord]] = {
    val whereClause =
      filterNot
        .map {
          case Nil => """WHERE "id" IN (NULL)"""
          case availableOrganizationIds =>
            s"""WHERE "id" IN ${availableOrganizationIds.map(id => s"'$id'").mkString("(", ",", ")")}"""
        }
        .getOrElse("")
    val sortClause = """ORDER BY created_at DESC"""

    (sql"""SELECT "id", "owner_id", "name", "created_at"
          | FROM "organization" """.stripMargin
      ++ Fragment.const(whereClause) ++ Fragment.const(sortClause) ++
      sql""" LIMIT $limit OFFSET $offset""".stripMargin).query[OrganizationRecord].to[List]
  }

  override def findByName(name: OrganizationName): ConnectionIO[Option[OrganizationRecord]] =
    sql"""SELECT "id", "owner_id", "name", "created_at"
         | FROM "organization"
         | WHERE "name"=$name""".stripMargin.query[OrganizationRecord].option

  override def find(pk: OrganizationID): ConnectionIO[Option[OrganizationRecord]] =
    sql"""SELECT "id", "owner_id", "name", "created_at"
         | FROM "organization"
         | WHERE "id"=$pk""".stripMargin.query[OrganizationRecord].option

  override def retrieve(pk: OrganizationID)(implicit show: Show[OrganizationID]): ConnectionIO[OrganizationRecord] =
    sql"""SELECT "id", "owner_id", "name", "created_at"
         | FROM "organization"
         | WHERE "id"=$pk""".stripMargin.query[OrganizationRecord].unique

  override def insert(e: OrganizationRecord): ConnectionIO[OrganizationID] =
    sql"""INSERT INTO "organization" ("id", "owner_id", "name", "created_at") 
         | VALUES (${e.id}, ${e.ownerId}, ${e.name}, ${e.createdAt})""".stripMargin.update
      .withUniqueGeneratedKeys[OrganizationID]("id")

  override def insertMany(es: Iterable[OrganizationRecord]): ConnectionIO[Unit] = ???

  override def update(e: OrganizationRecord): ConnectionIO[OrganizationRecord] =
    sql"""UPDATE "organization" 
         | SET "owner_id"=${e.ownerId}, "name"=${e.name}, "created_at"=${e.createdAt}
         | WHERE "id"=${e.id}""".stripMargin.update.withUniqueGeneratedKeys[OrganizationRecord](
      "id",
      "owner_id",
      "name",
      "created_at",
    )

  override def updateMany[M[_]](es: M[OrganizationRecord])(implicit evidence$1: Traverse[M]): ConnectionIO[Unit] =
    es.traverse(update).void

  override def delete(pk: OrganizationID): ConnectionIO[Unit] =
    sql"""DELETE FROM "organization" WHERE "id"=$pk""".update.run.void

  override def deleteMany(pks: Iterable[OrganizationID]): ConnectionIO[Unit] = ???

  override def exists(pk: OrganizationID): ConnectionIO[Boolean] = find(pk).map(_.isDefined)

  override def existsAtLeastOne(pks: Iterable[OrganizationID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[OrganizationID]): ConnectionIO[Boolean] = ???

}

private[organization] object OrganizationSQLImpl {

  def sync[F[_]](timeAlgebra: TimeAlgebra)(implicit F: Sync[F]): F[OrganizationSQL[ConnectionIO]] =
    F.pure(new OrganizationSQLImpl(timeAlgebra))

}
