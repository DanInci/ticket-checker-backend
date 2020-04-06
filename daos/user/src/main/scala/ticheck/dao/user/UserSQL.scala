package ticheck.dao.user

import ticheck.dao.user.models.UserTable
import ticheck.db._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/6/2020
  *
  */
trait UserSQL[H[_]] extends DAOAlgebra[H, UserTable, UserID]
