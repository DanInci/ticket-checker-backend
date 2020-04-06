package ticheck.dao.user

import ticheck.effect._
import ticheck.json._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
sealed trait UserRole {
  def asString: String
}

object UserRole {

  implicit def userRoleCodec: Codec[UserRole] = Codec.from(userRoleDecoder, userRoleEncoder)

  private val userRoleEncoder: Encoder[UserRole] = Encoder.apply[String].contramap(_.asString)
  private val userRoleDecoder: Decoder[UserRole] =
    Decoder.apply[String].emap(s => UserRole.fromString(s).leftMap(_.getMessage))

  private val OrganizationOwnerString = "OrganizationOwner"
  private val AdminString             = "Admin"
  private val PublisherString         = "Publisher"
  private val ValidatorString         = "Validator"
  private val UserString              = "User"

  final object OrganizationOwner extends UserRole {
    override def asString: String = OrganizationOwnerString
  }
  final object Admin extends UserRole {
    override def asString: String = AdminString
  }
  final object Publisher extends UserRole {
    override def asString: String = PublisherString
  }
  final object Validator extends UserRole {
    override def asString: String = ValidatorString
  }
  final object User extends UserRole {
    override def asString: String = UserString
  }

  private val rolesMap: Map[String, UserRole] =
    Map(
      OrganizationOwnerString -> OrganizationOwner,
      AdminString             -> Admin,
      PublisherString         -> Publisher,
      ValidatorString         -> Validator,
      UserString              -> User,
    )

  def fromString(roleString: String): Attempt[UserRole] = rolesMap.get(roleString) match {
    case None    => Attempt.raiseError(InvalidUserRoleAnomaly(roleString))
    case Some(r) => Attempt.pure(r)
  }

}
