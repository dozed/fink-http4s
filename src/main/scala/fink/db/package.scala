package fink

import cats.effect.{ContextShift, IO}
import doobie.Transactor

import scala.concurrent.ExecutionContext

package object db {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:fink", "fink", "fink"
  )


}
