package ticheck.algebra.organization.models

import ticheck.dao.organization.membership.models.OrganizationMembershipRecord
import ticheck.dao.organization.membership.{JoinedAt, OrganizationRole}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/18/2020
  *
  */
final case class Membership(
  role:     OrganizationRole,
  joinedAt: JoinedAt,
)

object Membership {
  import ticheck.json._

  implicit val jsonCodec: Codec[Membership] = derive.codec[Membership]

  def fromDAO(om: OrganizationMembershipRecord): Membership =
    Membership(
      om.role,
      om.joinedAt,
    )

}
