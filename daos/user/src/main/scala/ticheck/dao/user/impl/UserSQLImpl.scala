package ticheck.dao.user.impl

import ticheck.{Email, UserID}
import ticheck.effect._
import ticheck.dao.user.models.UserRecord
import ticheck.dao.user._
import ticheck.db._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
final private[user] class UserSQLImpl private (override val timeAlgebra: TimeAlgebra)
    extends UserSQL[ConnectionIO] with UserComposites {

  override def findByEmail(email: Email): ConnectionIO[Option[UserRecord]] =
    sql"""SELECT "id", "email", "hashed_password", "name", "created_at", "edited_at"
         | FROM "user"
         | WHERE "email"=$email""".stripMargin.query[UserRecord].option

  override def find(pk: UserID): ConnectionIO[Option[UserRecord]] =
    sql"""SELECT "id", "email", "hashed_password", "name", "created_at", "edited_at"
         | FROM "user"
         | WHERE "id"=$pk""".stripMargin.query[UserRecord].option

  override def retrieve(pk: UserID)(implicit show: Show[UserID]): ConnectionIO[UserRecord] =
    sql"""SELECT "id", "email", "hashed_password", "name", "created_at", "edited_at"
         | FROM "user"
         | WHERE "id"=$pk""".stripMargin.query[UserRecord].unique

  override def insert(e: UserRecord): ConnectionIO[UserID] =
    sql"""INSERT INTO "user" ("id", "email", "hashed_password", "name", "created_at", "edited_at")
         | VALUES (${e.id}, ${e.email}, ${e.hashedPassword}, ${e.name}, ${e.createdAt}, ${e.editedAt})""".stripMargin.update
      .withUniqueGeneratedKeys[UserID]("id")

  override def insertMany(es: Iterable[UserRecord]): ConnectionIO[Unit] = ???

  override def update(e: UserRecord): ConnectionIO[UserRecord] =
    sql"""UPDATE "user"
         | SET "email"=${e.email}, "hashed_password"=${e.hashedPassword}, "name"=${e.name}, "created_at"=${e.createdAt}, "edited_at"=${e.editedAt}
         | WHERE "id"=${e.id}""".stripMargin.update.withUniqueGeneratedKeys[UserRecord](
      "id",
      "organization_id",
      "email",
      "hashed_password",
      "name",
      "created_at",
      "edited_at",
    )

  override def updateMany[M[_]](es: M[UserRecord])(implicit t: Traverse[M]): ConnectionIO[Unit] = ???

  override def delete(pk: UserID): ConnectionIO[Unit] =
    sql"""DELETE FROM "user" WHERE "id"=$pk""".stripMargin.update.run.void

  override def deleteMany(pks: Iterable[UserID]): ConnectionIO[Unit] = ???

  override def exists(pk: UserID): ConnectionIO[Boolean] = find(pk).map(_.isDefined)

  override def existsAtLeastOne(pks: Iterable[UserID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[UserID]): ConnectionIO[Boolean] = ???

}

private[user] object UserSQLImpl {

  def sync[F[_]](timeAlgebra: TimeAlgebra)(implicit F: Sync[F]): F[UserSQL[ConnectionIO]] =
    F.pure(new UserSQLImpl(timeAlgebra))

}
