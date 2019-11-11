package fink

import cats.effect.{ContextShift, IO}
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux

import scala.concurrent.ExecutionContext

package object db {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  var xa: Transactor.Aux[IO, Unit] = null


}
