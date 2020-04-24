package ticheck.email

import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 4/24/2020
  *
  */
trait ModuleEmailAlgebra[F[_]] {

  implicit protected def F: Async[F]

  implicit protected def blockingShifter: BlockingShifter[F]

  protected def emailConfig: EmailConfig

  def emailAlgebra: F[EmailAlgebra[F]] = _emailAlgebra

  private lazy val _emailAlgebra: F[EmailAlgebra[F]] =
    impl.EmailAlgebraImpl.async[F](emailConfig)

}
