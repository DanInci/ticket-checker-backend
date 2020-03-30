package ticheck.time

import ticheck.effect.Sync

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait ModuleTimeAlgebra[F[_]] {

  implicit protected def S: Sync[F]

  implicit protected def timeConfig: TimeConfig

  protected def timeAlgebra: F[TimeAlgebra] = _timeAlgebra

  private lazy val _timeAlgebra: F[TimeAlgebra] = impl.TimeAlgebraImpl.sync(timeConfig)

}
