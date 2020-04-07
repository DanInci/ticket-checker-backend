package ticheck.algebra.user.models

import ticheck.UserID
import ticheck.dao.user._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/7/2020
  *
  */
final case class User(
  id:   UserID,
  emai: Email,
  name: Name,
  role: UserRole,
)
