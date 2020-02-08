package fink.web

import java.time.Instant
import java.time.temporal.ChronoUnit

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

        val twoWeeks = Instant.now.plus(14, ChronoUnit.DAYS)
        val twoWeeksHttpDate = HttpDate.unsafeFromInstant(twoWeeks)

        val cookie = ResponseCookie(
          "ui",
          jwt,
          path = Some("/"),
          expires = Some(twoWeeksHttpDate),
          httpOnly = true
        )

        Ok().map(_.addCookie(cookie))

    }


}
