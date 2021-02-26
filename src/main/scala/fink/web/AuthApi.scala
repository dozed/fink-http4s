package fink.web

import cats.effect._
import cats.syntax.show._
import fink.World._
import fink.data.JsonInstances._
import fink.data.Operation
import fink.auth.Authentication
import fink.syntax._
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object AuthApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case req@POST -> Root / "login" =>
      for {
        op <- req.decodeJson[Operation.Login]
        res <- {
          Authentication.login(op.username, op.password)
        }
      } yield {
        res
      }

    case POST -> Root / "logout" =>
      Authentication.logout()

    case req@GET -> Root / "me" =>

      for {
        user <- req.authenticateUser
        userJson = {
          Json.obj(
            "id" -> Json.fromLong(user.id),
            "name" -> Json.fromString(user.name),
            "roles" -> Json.fromValues(user.roles.map(r => Json.fromString(r.show)))
          )
        }
        res <- Ok(userJson)
      } yield {
        res
      }
  }


}
