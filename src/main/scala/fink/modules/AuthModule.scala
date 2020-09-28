package fink.modules

import java.time.Instant
import java.time.temporal.ChronoUnit

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.World
import fink.World._
import fink.data._
import fink.db.UserDAO
import io.circe.Json
import org.http4s._
import pdi.jwt.JwtCirce

object AuthModule {

  def fetchUser(req: Request[IO]): IO[Either[ErrorCode, User]] = {
    readUserId(req) match {
      case Left(error) => IO.pure(Left(error))
      case Right(userId) =>
        UserDAO.findById(userId).transact(xa).map {
          case None => Left(ErrorCode.NotFound("Could not find user"))
          case Some(user) => Right(user)
        }
    }
  }

  def readUserId(req: Request[IO]): Either[ErrorCode, UserId] = {
    for {
      header <- headers.Cookie.from(req.headers).toRight(ErrorCode.ParseError("Cookie parsing error"))
      cookie <- {
        header.values.toList
          .find(_.name == World.config.authConfig.cookieName)
          .toRight(ErrorCode.ParseError("Couldn't find the authcookie"))
      }
      token <- {
        JwtCirce.decodeJson(
          cookie.content,
          World.config.authConfig.key,
          Seq(World.config.authConfig.algo)
        ).toEither.leftMap(_ => ErrorCode.ParseError("Couldnt read JWT"))
      }
      authUserId <- {
        token.hcursor
          .downField("authUserId").as[Long]
          .leftMap(_ => ErrorCode.ParseError("Couldnt read authUserId from JWT"))
      }
    } yield {
      authUserId
    }
  }

  def login(username: String, password: String): IO[Response[IO]] = {
    if (username === "admin" && password === "admin") {
      val userId = 1
      val res = mkUserLoginResponse(userId)
      IO.pure(res)
    } else {
      IO.pure(Response(status = Status.Forbidden))
    }
  }

  def mkUserLoginResponse(userId: Long): Response[IO] = {
    val jwt = JwtCirce.encode(
      Json.obj("authUserId" -> Json.fromLong(userId)),
      World.config.authConfig.key,
      World.config.authConfig.algo
    )

    val twoWeeks = Instant.now.plus(14, ChronoUnit.DAYS)
    val twoWeeksHttpDate = HttpDate.unsafeFromInstant(twoWeeks)

    val cookie = ResponseCookie(
      World.config.authConfig.cookieName,
      jwt,
      path = Some("/"),
      expires = Some(twoWeeksHttpDate),
      httpOnly = true
    )

    Response(status = Status.Ok).addCookie(cookie)
  }

  def logout(): IO[Response[IO]] = {
    val cookie = ResponseCookie(
      World.config.authConfig.cookieName,
      "",
      path = Some("/"),
      httpOnly = true
    )

    IO.pure(Response(status = Status.Ok).removeCookie(cookie))
  }

}
