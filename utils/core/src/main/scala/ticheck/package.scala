import java.time.OffsetDateTime

import busymachines.pureharm
import io.chrisdavenport.fuuid

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object ticheck
    extends pureharm.PureharmCoreTypeDefinitions with pureharm.anomaly.AnomalyParamtersImplicits
    with pureharm.anomaly.PureharmAnomalyTypeDefinitions {

  type Email = Email.Type

  final type FUUID = fuuid.FUUID
  final val FUUID: fuuid.FUUID.type = fuuid.FUUID

  object UserID extends PhantomFUUID
  type UserID = UserID.Type

  object Name extends PhantomType[String]
  type Name = Name.Type

  object OrganizationID extends PhantomFUUID
  type OrganizationID = OrganizationID.Type

  object OrganizationMembershipID extends PhantomFUUID
  type OrganizationMembershipID = OrganizationMembershipID.Type

  object OrganizationInviteID extends PhantomFUUID
  type OrganizationInviteID = OrganizationInviteID.Type

  object TicketID extends PhantomType[String]
  type TicketID = TicketID.Type

  object CreatedAt extends PhantomType[OffsetDateTime]
  type CreatedAt = CreatedAt.Type

  object PageNumber extends PhantomType[Int]
  type PageNumber = PageNumber.Type

  object PageSize extends PhantomType[Int]
  type PageSize = PageSize.Type

  object Offset extends PhantomType[Int]
  type Offset = Offset.Type

  object Limit extends PhantomType[Int]
  type Limit = Limit.Type

  object Count extends PhantomType[Int]
  type Count = Count.Type

}
