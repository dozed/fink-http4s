package fink

import cats.effect.IO
import fink.data.AppConfig
import doobie._

case class World(
  db: Transactor[IO],
  config: AppConfig
)
