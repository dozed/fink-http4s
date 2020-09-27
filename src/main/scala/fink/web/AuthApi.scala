package fink.web

import cats.effect._
import cats.implicits._
import fink.World._
import fink.modules.AuthModule
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object AuthApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / "login" =>
      AuthModule.login(1)

    case GET -> Root / "logout" =>
      AuthModule.logout()

    case req@GET -> Root / "me" =>

      for {
        user <- AuthModule.fetchUser(req).rethrow
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
