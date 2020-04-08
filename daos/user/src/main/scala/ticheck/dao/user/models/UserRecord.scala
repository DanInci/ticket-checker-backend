package ticheck.dao.user.models

import ticheck.{CreatedAt, Email, Name, UserID}
import ticheck.dao.user._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
case class UserRecord(
  id:             UserID,
  email:          Email,
  hashedPassword: HashedPassword,
  name:           Name,
  createdAt:      CreatedAt,
  editedAt:       Option[EditedAt],
)
