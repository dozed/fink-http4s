package fink.web

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.EntityEncoders._
import fink.World._
import fink.data.{Operation, _}
import fink.db.{PageDAO, TagDAO}
import fink.modules.AuthModule._
import org.http4s._
import org.http4s.dsl.io._

object TagApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "tags" =>

      TagDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

  }

}
