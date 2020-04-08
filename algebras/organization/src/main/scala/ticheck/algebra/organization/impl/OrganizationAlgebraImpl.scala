package ticheck.algebra.organization.impl

import ticheck.OrganizationID
import ticheck.algebra.organization.models.OrganizationProfile
import ticheck.algebra.organization.{OrganizationAlgebra, OrganizationModuleAlgebra}
import ticheck.dao.organization.OrganizationSQL
import ticheck.dao.organization.invite.OrganizationInviteSQL
import ticheck.dao.organization.membership.OrganizationMembershipSQL
import ticheck.db._
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[organization] class OrganizationAlgebraImpl[F[_]] private (
  organizationSQL:           OrganizationSQL[ConnectionIO],
  organizationInviteSQL:     OrganizationInviteSQL[ConnectionIO],
  organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
)(implicit F:                Async[F], transactor: Transactor[F])
    extends OrganizationAlgebra[F] with DBOperationsAlgebra[F] {

  override def getById(id: OrganizationID): F[OrganizationProfile] = ???

}

private[organization] object OrganizationAlgebraImpl {

  def async[F[_]: Async: Transactor](
    organizationSQL:           OrganizationSQL[ConnectionIO],
    organizationInviteSQL:     OrganizationInviteSQL[ConnectionIO],
    organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
  ): F[OrganizationModuleAlgebra[F]] =
    Async[F].pure(
      new OrganizationAlgebraImpl[F](organizationSQL, organizationInviteSQL, organizationMembershipSQL),
    )

}
