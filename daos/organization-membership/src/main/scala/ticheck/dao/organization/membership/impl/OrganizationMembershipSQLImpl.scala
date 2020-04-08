package ticheck.dao.organization.membership.impl

import ticheck.effect._
import ticheck.{OrganizationMembershipID, UserID}
import ticheck.dao.organization.membership.OrganizationMembershipSQL
import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.db._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[membership] class OrganizationMembershipSQLImpl[F[_]] private (override val timeAlgebra: TimeAlgebra)
    extends OrganizationMembershipSQL[ConnectionIO] with OrganizationMembershipComposites {

  override def getByUserID(userId: UserID): ConnectionIO[List[OrganizationMembershipRecord]] =
    sql"""SELECT "id", "user_id", "organization_id", "invite_id", "role", "joined_at"
         | FROM "organization_membership"
         | WHERE "user_id"=$userId""".stripMargin.query[OrganizationMembershipRecord].to[List]

  override def find(pk: OrganizationMembershipID): ConnectionIO[Option[OrganizationMembershipRecord]] =
    sql"""SELECT "id", "user_id", "organization_id", "invite_id", "role", "joined_at"
         | FROM "organization_membership"
         | WHERE "id"=$pk""".stripMargin.query[OrganizationMembershipRecord].option

  override def retrieve(pk: OrganizationMembershipID)(
    implicit show:          Show[OrganizationMembershipID],
  ): ConnectionIO[OrganizationMembershipRecord] =
    sql"""SELECT "id", "user_id", "organization_id", "invite_id", "role", "joined_at"
         | FROM "organization_membership"
         | WHERE "id"=$pk""".stripMargin.query[OrganizationMembershipRecord].unique

  override def insert(e: OrganizationMembershipRecord): ConnectionIO[OrganizationMembershipID] =
    sql"""INSERT INTO "organization_membership" ("id", "user_id", "organization_id", "invite_id", "role", "joined_at")
         | VALUES(${e.id}, ${e.userId}, ${e.organizationId}, ${e.inviteId}, ${e.role}, ${e.joinedAt})""".stripMargin.update
      .withUniqueGeneratedKeys[OrganizationMembershipID]("id")

  override def insertMany(es: Iterable[OrganizationMembershipRecord]): ConnectionIO[Unit] = ???

  override def update(e: OrganizationMembershipRecord): ConnectionIO[OrganizationMembershipRecord] =
    sql"""UPDATE "organization_membership"
         | SET "user_id"=${e.userId}, "organization_id"=${e.organizationId}, "invite_id"=${e.inviteId}, "role"=${e.role}, "joined_at"=${e.joinedAt}
         | WHERE "id"=${e.id}""".stripMargin.update
      .withUniqueGeneratedKeys[OrganizationMembershipRecord](
        "id",
        "user_id",
        "organization_id",
        "invite_id",
        "role",
        "joined_at",
      )

  override def updateMany[M[_]](es: M[OrganizationMembershipRecord])(
    implicit evidence$1:            Traverse[M],
  ): ConnectionIO[Unit] = ???

  override def delete(pk: OrganizationMembershipID): ConnectionIO[Unit] =
    sql"""DELETE FROM "organization_membership" WHERE "id"=$pk""".update.run.void

  override def deleteMany(pks: Iterable[OrganizationMembershipID]): ConnectionIO[Unit] = ???

  override def exists(pk: OrganizationMembershipID): ConnectionIO[Boolean] = find(pk).map(_.isDefined)

  override def existsAtLeastOne(pks: Iterable[OrganizationMembershipID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[OrganizationMembershipID]): ConnectionIO[Boolean] = ???
}

private[membership] object OrganizationMembershipSQLImpl {

  def sync[F[_]](timeAlgebra: TimeAlgebra)(implicit F: Sync[F]): F[OrganizationMembershipSQL[ConnectionIO]] =
    F.pure(new OrganizationMembershipSQLImpl(timeAlgebra))

}
