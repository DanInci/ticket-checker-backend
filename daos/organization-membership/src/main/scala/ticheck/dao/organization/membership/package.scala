package ticheck.dao.organization

import java.time.OffsetDateTime

import busymachines.pureharm.phantom.PhantomType

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
package object membership {

  object JoinedAt extends PhantomType[OffsetDateTime]
  type JoinedAt = JoinedAt.Type

  object SoldTicketsNo extends PhantomType[Int]
  type SoldTicketsNo = SoldTicketsNo.Type

  object ValidatedTicketsNo extends PhantomType[Int]
  type ValidatedTicketsNo = ValidatedTicketsNo.Type

}
