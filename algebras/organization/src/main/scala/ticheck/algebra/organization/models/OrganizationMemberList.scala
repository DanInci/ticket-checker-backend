package ticheck.algebra.organization.models

import ticheck.{Name, UserID}
import ticheck.dao.organization.membership.{JoinedAt, OrganizationRole}

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final case class OrganizationMemberList(
  userId:   UserID,
  name:     Name,
  role:     OrganizationRole,
  joinedAt: JoinedAt,
)

object OrganizationMemberList {
  import ticheck.json._

  implicit val jsonCodec: Codec[OrganizationMemberList] = derive.codec[OrganizationMemberList]

}
