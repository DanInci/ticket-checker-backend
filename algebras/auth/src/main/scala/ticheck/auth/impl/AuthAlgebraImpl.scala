package ticheck.auth.impl

import ticheck.{CreatedAt, InconsistentState, UserID}
import ticheck.auth._
import ticheck.auth.models._
import ticheck.dao.organization.membership.OrganizationMembershipSQL
import ticheck.dao.user.UserSQL
import ticheck.dao.user.models.UserRecord
import ticheck.db._
import ticheck.effect._
import ticheck.time.TimeAlgebra

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
final private[auth] class AuthAlgebraImpl[F[_]] private (
  authConfig:                JWTAuthConfig,
  timeAlgebra:               TimeAlgebra,
  userSQL:                   UserSQL[ConnectionIO],
  organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
)(
  implicit F: Async[F],
  transactor: Transactor[F],
) extends AuthAlgebra[F] with DBOperationsAlgebra[F] {

  override def convert(rawAuthCtx: RawAuthCtx): F[UserAuthCtx] = transact {
    for {
      user <- userSQL
        .find(rawAuthCtx.userId)
        .flattenOption(
          InconsistentState(
            what     = s"Cannot convert RawAuthCtx to UserAuthCtx because user with id ${rawAuthCtx.userId} does not exist",
            where    = "AuthAlgebraImpl.convert",
            causedBy = None,
          ),
        )
      memberships <- organizationMembershipSQL.getByUserID(rawAuthCtx.userId)
      userAuthCtx = UserAuthCtx.from(user, memberships)
    } yield userAuthCtx
  }

  override def register(regData: RegistrationRequest): F[Unit] = transact {
    for {
      _              <- checkRegistrationData(regData)
      id             <- UserID.generate[ConnectionIO]
      now            <- timeAlgebra.now[ConnectionIO].map(CreatedAt.spook)
      hashedPassword <- BCryptPasswordHash.forPassword[ConnectionIO](regData.password)
      userDAO = UserRecord(id, regData.email, hashedPassword, regData.name, now, None)
      _ <- userSQL.insert(userDAO)
    } yield ()
  }

  override def login(loginData: LoginRequest): F[JWTAuthToken] = transact {
    for {
      user <- userSQL.findByEmail(loginData.email).flattenOption(LoginFailedAnomaly)
      _ <- BCryptPasswordHash
        .check[ConnectionIO](loginData.password, user.hashedPassword)
        .ifFalseRaise(LoginFailedAnomaly)
      membershipOrganizationIds <- organizationMembershipSQL.getByUserID(user.id).map(_.map(_.organizationId))
      authCtx = RawAuthCtx(user.id, membershipOrganizationIds)
      token <- JWTAuthToken.create[ConnectionIO, RawAuthCtx](authConfig)(authCtx)
    } yield token
  }

  private def checkRegistrationData(regData: RegistrationRequest): ConnectionIO[Unit] =
    for {
      _ <- regData.password
        .matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?=.*[A-Z])(?=.*[a-z]).*$")
        .pure[ConnectionIO]
        .ifFalseRaise(PasswordDoesNotMeetCriteriaAnomaly)
      _ <- userSQL.findByEmail(regData.email).ifSomeRaise(EmailAlreadyRegisteredAnomaly(regData.email))
    } yield ()

}

private[auth] object AuthAlgebraImpl {

  def async[F[_]: Async: Transactor](
    authConfig:                JWTAuthConfig,
    timeAlgebra:               TimeAlgebra,
    userSQL:                   UserSQL[ConnectionIO],
    organizationMembershipSQL: OrganizationMembershipSQL[ConnectionIO],
  ): F[AuthAlgebra[F]] =
    Async[F].pure(new AuthAlgebraImpl[F](authConfig, timeAlgebra, userSQL, organizationMembershipSQL))

}
