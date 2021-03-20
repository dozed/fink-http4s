package fink.db

import doobie._
import doobie.implicits._
import cats.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext

import fink.World.xa

object DbSetup {

  val drop: ConnectionIO[Int] = Update0(scala.io.Source.fromFile("./sql/drop.sql").mkString, None).run

  val create: ConnectionIO[Int] = Update0(scala.io.Source.fromFile("./sql/create.sql").mkString, None).run

  val data: ConnectionIO[Int] = Update0(scala.io.Source.fromFile("./sql/data.sql").mkString, None).run

  val setupDbWithoutData: ConnectionIO[Unit] = {
    for {
      _ <- drop
      _ <- create
    } yield {
      ()
    }
  }

  val setupDb: ConnectionIO[Unit] = {
    for {
      _ <- drop
      _ <- create
      _ <- data
    } yield {
      ()
    }
  }

}

object DbSetupApp extends App {


  import DbSetup._

  implicit val cs = IO.contextShift(ExecutionContext.global)

  setupDb.transact(xa).unsafeRunSync()

}
