package ticheck.rest.routes

import io.chrisdavenport.fuuid.http4s.FUUIDVar
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ticheck.effect._
import ticheck.organizer.organization.OrganizationOrganizer
import ticheck.rest.UserAuthCtxRoutes

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final private[rest] class OrganizationRoutes[F[_]](
  private val organizationOrganizer: OrganizationOrganizer[F],
)(implicit val F:                    Async[F])
    extends Http4sDsl[F] {

  private val organizationRestRoutes: UserAuthCtxRoutes[F] = UserAuthCtxRoutes[F] {
    case GET -> Root / "organization" / FUUIDVar(orgId) as user =>
      for {
        resp <- Ok()
      } yield resp
  }

  private val registerOrganizationRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "organization" =>
      for {
        resp <- Ok()
      } yield resp
  }

  val authedRoutes: UserAuthCtxRoutes[F] = organizationRestRoutes

  val routes: HttpRoutes[F] = registerOrganizationRoute

}
