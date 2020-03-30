package ticheck

import ticheck.effect._

/**
  * "Inconsistent State", technically should never happen, and you should
  * really try to restructure your code so you never get here.
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
final case class InconsistentState(what: String, where: String, override val causedBy: Option[Throwable])
    extends InconsistentStateCatastrophe(
      message  = s"We have reached some inconsistent state, this is definitely a bug. Where: '$where'. What: '$what'",
      causedBy = causedBy,
    ) {
  override val id: AnomalyID = AnomalyIDs.InconsistentStateAnomalyID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "what"  -> what,
    "where" -> where,
  )
}

object InconsistentState {

  def unit[F[_]](what: String, where: String)(implicit F: MonadAttempt[F]): F[Unit] =
    F.raiseError(InconsistentState(what, where, Option.empty))

  def fail[F[_], T](what: String, where: String)(implicit F: MonadAttempt[F]): F[T] =
    F.raiseError[T](InconsistentState(what, where, Option.empty))
}
