package ticheck.dao.organization.impl

import ticheck._
import ticheck.db._
import ticheck.effect._
import ticheck.dao.organization.OrganizationSQL
import ticheck.dao.organization.models.OrganizationTable

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
private[organization] object OrganizationSQLImpl extends OrganizationSQL[ConnectionIO] with OrganizationComposites {

  override def find(pk: OrganizationID): ConnectionIO[Option[OrganizationTable]] = ???

  override def retrieve(pk: OrganizationID)(implicit show: Show[OrganizationID]): ConnectionIO[OrganizationTable] = ???

  override def insert(e: OrganizationTable): ConnectionIO[OrganizationID] = ???

  override def insertMany(es: Iterable[OrganizationTable]): ConnectionIO[Unit] = ???

  override def update(e: OrganizationTable): ConnectionIO[OrganizationTable] = ???

  override def updateMany[M[_]](es: M[OrganizationTable])(implicit evidence$1: Traverse[M]): ConnectionIO[Unit] = ???

  override def delete(pk: OrganizationID): ConnectionIO[Unit] = ???

  override def deleteMany(pks: Iterable[OrganizationID]): ConnectionIO[Unit] = ???

  override def exists(pk: OrganizationID): ConnectionIO[Boolean] = ???

  override def existsAtLeastOne(pks: Iterable[OrganizationID]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[OrganizationID]): ConnectionIO[Boolean] = ???

}
