package ticheck.dao.user

import ticheck._
import ticheck.dao.user.models.UserRecord
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait UserSQL[H[_]] extends DAOAlgebra[H, UserRecord, UserID] {

  def findByEmail(email: Email): H[Option[UserRecord]]

}
