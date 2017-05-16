package fink

import fink.data.AppConfig

import doobie.imports._

case class World(
  db: Transactor[IOLite],
  config: AppConfig
)
