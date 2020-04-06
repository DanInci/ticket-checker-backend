package ticheck.dao

import java.time.{LocalDate, OffsetDateTime}

import ticheck.PhantomType
import io.chrisdavenport.fuuid.FUUID
import ticheck.{OrganizationID, TicketID}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
package object ticket {

  type TicketPK = (TicketID, OrganizationID)

  object SoldTo extends PhantomType[String]
  type SoldTo = SoldTo.Type

  object SoldToBirthday extends PhantomType[LocalDate]
  type SoldToBirthday = SoldToBirthday.Type

  object SoldToTelephone extends PhantomType[String]
  type SoldToTelephone = SoldToTelephone.Type

  object SoldBy extends PhantomType[FUUID]
  type SoldBy = SoldBy.Type

  object SoldByName extends PhantomType[String]
  type SoldByName = SoldByName.Type

  object SoldAt extends PhantomType[OffsetDateTime]
  type SoldAt = SoldAt.Type

  object ValidatedBy extends PhantomType[FUUID]
  type ValidatedBy = ValidatedBy.Type

  object ValidatedByName extends PhantomType[String]
  type ValidatedByName = ValidatedByName.Type

  object ValidatedAt extends PhantomType[OffsetDateTime]
  type ValidatedAt = ValidatedAt.Type

}
