package fink.web

import cats.effect._
import fink.World._
import io.circe.Json
import org.http4s._
import org.http4s.dsl.io._
import pdi.jwt.{JwtAlgorithm, JwtCirce}

object AuthApi {

    val routes = HttpRoutes.of[IO] {

      case GET -> Root / "login" =>

        val jwt = JwtCirce.encode(Json.obj("authUserId" -> Json.fromLong(1)), key, JwtAlgorithm.HS256)

        Ok().map(_.addCookie("ui", jwt))

    }


}
