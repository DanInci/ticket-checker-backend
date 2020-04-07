package ticheck.dao.user.models

import ticheck.{CreatedAt, OrganizationID, UserID}
import ticheck.dao.user._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
case class UserRecord(
  id:             UserID,
  organizationId: OrganizationID,
  email:          Email,
  hashedPassword: HashedPassword,
  name:           Name,
  role:           UserRole,
  createdAt:      CreatedAt,
  editedAt:       Option[EditedAt],
)
