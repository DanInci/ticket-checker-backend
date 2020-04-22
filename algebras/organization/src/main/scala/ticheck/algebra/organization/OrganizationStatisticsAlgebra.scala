package ticheck.algebra.organization

import ticheck.{Count, OrganizationID}
import ticheck.dao.organization.membership.OrganizationRole

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/22/2020
  *
  */
trait OrganizationStatisticsAlgebra[F[_]] {

  def getOrganizationMembersCount(
    organizationId: OrganizationID,
    byRole:         Option[OrganizationRole],
    searchValue:    Option[String],
  ): F[Count]

}
