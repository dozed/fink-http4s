package fink.web

import cats.effect._
import cats.implicits._
import fink.World._
import fink.EntityEncoders._
import fink.syntax._
import fink.data.JsonInstances._
import fink.data.Operation
import fink.modules.AuthModule
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.circe._

object AuthApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case req@POST -> Root / "login" =>
      for {
        op <- req.decodeJson[Operation.Login]
        res <- {
          AuthModule.login(op.username, op.password)
        }
      } yield {
        res
      }

    case POST -> Root / "logout" =>
      AuthModule.logout()

    case req@GET -> Root / "me" =>

      for {
        user <- req.authenticateUser
        userJson = {
          Json.obj(
            "id" -> Json.fromLong(user.id),
            "name" -> Json.fromString(user.name)
          )
        }
        res <- Ok(userJson)
      } yield {
        res
      }
  }


}
