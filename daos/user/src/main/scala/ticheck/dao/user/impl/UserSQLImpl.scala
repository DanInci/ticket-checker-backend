package ticheck.dao.user.impl

import prepy.syntax._
import ticheck.effect._
import ticheck.dao.user.models.UserTable
import ticheck.dao.user._
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
private[user] object UserSQLImpl extends UserSQL[ConnectionIO] with UserComposites with ValidatedQueryOps {

  override def find(pk: UserID): ConnectionIO[Option[UserTable]] = ???

  override def retrieve(pk: UserID)(implicit show: Show[UserID]): ConnectionIO[UserTable] = ???

  override def insert(e: UserTable): ConnectionIO[UserID] = ???

  override def insertMany(es: Iterable[UserTable]): ConnectionIO[Unit] = ???

  override def update(e: UserTable): ConnectionIO[UserTable] = ???

  override def updateMany[M[_]](es: M[UserTable])(implicit t: Traverse[M]): ConnectionIO[Unit] = ???

  override def delete(pk: UserID): ConnectionIO[Unit] = ???

  override def deleteMany(pks: Iterable[UserID]): ConnectionIO[Unit] = ???

  override def exists(pk: UserID): ConnectionIO[Boolean] = ???

  override def existsAtLeastOne(pks: Iterable[UserID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[UserID]): ConnectionIO[Boolean] = ???

}
