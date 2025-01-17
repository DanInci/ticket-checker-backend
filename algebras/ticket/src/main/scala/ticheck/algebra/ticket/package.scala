package ticheck.algebra

import java.time.LocalDateTime

import ticheck.PhantomType

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/8/2020
  *
  */
package object ticket {

  type TicketModuleAlgebra[F[_]] = TicketAlgebra[F] with TicketStatisticsAlgebra[F]

  object IsValidated extends PhantomType[Boolean]
  type IsValidated = IsValidated.Type

  object StatisticsSize extends PhantomType[Int]
  type StatisticsSize = StatisticsSize.Type

  object StatisticsTimestamp extends PhantomType[LocalDateTime]
  type StatisticsTimestamp = StatisticsTimestamp.Type

}
