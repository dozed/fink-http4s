package fink

import cats.effect.{ContextShift, IO, Timer}
import fink.data.AppConfig
import doobie._
import pdi.jwt.JwtAlgorithm

import scala.concurrent.ExecutionContext

case class World(
  db: Transactor[IO],
  config: AppConfig
)

object World {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  var xa: Transactor[IO] = null

  var config: AppConfig = null

  val key = "secretK3y"

  val algo = JwtAlgorithm.HS256



}