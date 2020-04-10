package ticheck.dao.organization.invite.impl

import ticheck.{Email, OrganizationID, OrganizationInviteID}
import ticheck.dao.organization.invite.{InviteCode, OrganizationInviteSQL}
import ticheck.dao.organization.invite.models.OrganizationInviteRecord
import ticheck.db._
import ticheck.effect._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[invite] class OrganizationInviteSQLImpl private (override val timeAlgebra: TimeAlgebra)
    extends OrganizationInviteSQL[ConnectionIO] with OrganizationInviteComposites {

  override def findForOrganizationByEmail(
    id:    OrganizationID,
    email: Email,
  ): ConnectionIO[Option[OrganizationInviteRecord]] =
    sql"""SELECT "id", "organization_id", "email", "code", "status", "answered_at", "invited_at"
         | FROM "organization_invite"
         | WHERE "organization_id"=$id AND "email"=$email""".stripMargin.query[OrganizationInviteRecord].option

  override def findByInvitationCode(code: InviteCode): ConnectionIO[Option[OrganizationInviteRecord]] =
    sql"""SELECT "id", "organization_id", "email", "code", "status", "answered_at", "invited_at"
         | FROM "organization_invite"
         | WHERE "code"=$code""".stripMargin.query[OrganizationInviteRecord].option

  override def find(pk: OrganizationInviteID): ConnectionIO[Option[OrganizationInviteRecord]] =
    sql"""SELECT "id", "organization_id", "email", "code", "status", "answered_at", "invited_at"
         | FROM "organization_invite"
         | WHERE "id"=$pk""".stripMargin.query[OrganizationInviteRecord].option

  override def retrieve(pk: OrganizationInviteID)(
    implicit show:          Show[OrganizationInviteID],
  ): ConnectionIO[OrganizationInviteRecord] =
    sql"""SELECT "id", "organization_id", "email", "code", "status", "answered_at", "invited_at"
         | FROM "organization_invite"
         | WHERE "id"=$pk""".stripMargin
      .query[OrganizationInviteRecord]
      .unique

  override def insert(e: OrganizationInviteRecord): ConnectionIO[OrganizationInviteID] =
    sql"""INSERT INTO "organization_invite" ("id", "organization_id", "email", "code", "status", "answered_at", "invited_at")
         | VALUES(${e.id}, ${e.organizationId}, ${e.email}, ${e.code}, ${e.status}, ${e.answeredAt}, ${e.invitedAt})""".stripMargin.update
      .withUniqueGeneratedKeys[OrganizationInviteID]("id")

  override def insertMany(es: Iterable[OrganizationInviteRecord]): ConnectionIO[Unit] = ???

  override def update(e: OrganizationInviteRecord): ConnectionIO[OrganizationInviteRecord] =
    sql"""UPDATE "organization_invite"
         | SET "organization_id"=${e.organizationId}, "email"=${e.email}, "code"=${e.code}, "status"=${e.status}, "answered_at"=${e.answeredAt}, "invited_at"=${e.invitedAt}
         | WHERE "id"=${e.id} """.stripMargin.update.withUniqueGeneratedKeys[OrganizationInviteRecord](
      "id",
      "organization_id",
      "email",
      "code",
      "status",
      "answered_at",
      "invited_at",
    )

  override def updateMany[M[_]](es: M[OrganizationInviteRecord])(implicit evidence$1: Traverse[M]): ConnectionIO[Unit] =
    es.traverse(update).void

  override def delete(pk: OrganizationInviteID): ConnectionIO[Unit] =
    sql"""DELETE FROM "organization_invite" WHERE "id"=$pk""".stripMargin.update.run.void

  override def deleteMany(pks: Iterable[OrganizationInviteID]): ConnectionIO[Unit] = ???

  override def exists(pk: OrganizationInviteID): ConnectionIO[Boolean] = find(pk).map(_.isDefined)

  override def existsAtLeastOne(pks: Iterable[OrganizationInviteID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[OrganizationInviteID]): ConnectionIO[Boolean] = ???

}

private[invite] object OrganizationInviteSQLImpl {

  def sync[F[_]](timeAlgebra: TimeAlgebra)(implicit F: Sync[F]): F[OrganizationInviteSQL[ConnectionIO]] =
    F.pure(new OrganizationInviteSQLImpl(timeAlgebra))

}
