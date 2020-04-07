package ticheck.dao.organization.membership

import ticheck.effect._
import ticheck.json._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
sealed trait OrganizationRole {
  def asString: String
}

object OrganizationRole {

  implicit def userRoleCodec: Codec[OrganizationRole] = Codec.from(userRoleDecoder, userRoleEncoder)

  private val userRoleEncoder: Encoder[OrganizationRole] = Encoder.apply[String].contramap(_.asString)
  private val userRoleDecoder: Decoder[OrganizationRole] =
    Decoder.apply[String].emap(s => OrganizationRole.fromString(s).leftMap(_.getMessage))

  private val OrganizationOwnerString = "OrganizationOwner"
  private val AdminString             = "Admin"
  private val PublisherString         = "Publisher"
  private val ValidatorString         = "Validator"
  private val UserString              = "User"

  final object OrganizationOwner extends OrganizationRole {
    override def asString: String = OrganizationOwnerString
  }
  final object Admin extends OrganizationRole {
    override def asString: String = AdminString
  }
  final object Publisher extends OrganizationRole {
    override def asString: String = PublisherString
  }
  final object Validator extends OrganizationRole {
    override def asString: String = ValidatorString
  }
  final object User extends OrganizationRole {
    override def asString: String = UserString
  }

  private val rolesMap: Map[String, OrganizationRole] =
    Map(
      OrganizationOwnerString -> OrganizationOwner,
      AdminString             -> Admin,
      PublisherString         -> Publisher,
      ValidatorString         -> Validator,
      UserString              -> User,
    )

  def fromString(roleString: String): Attempt[OrganizationRole] = rolesMap.get(roleString) match {
    case None    => Attempt.raiseError(InvalidOrganizationRoleAnomaly(roleString))
    case Some(r) => Attempt.pure(r)
  }

  def unsafe(s: String): OrganizationRole = this.fromString(s) match {
    case Left(e)      => throw e
    case Right(value) => value
  }

}
