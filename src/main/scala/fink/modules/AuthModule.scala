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
import org.http4s.dsl.io._
import org.mindrot.jbcrypt.BCrypt
import pdi.jwt.JwtCirce

object AuthModule {

  def authenticateUser(req: Request[IO]): IO[User] = {
    fetchUser(req).flatMap {
      case Left(e) => IO.raiseError(e)
      case Right(user) => IO.pure(user)
    }
  }

  def loadUser(req: Request[IO])(f: User => IO[Response[IO]]): IO[Response[IO]] = {
    fetchUser(req).flatMap {
      case Left(e) => Forbidden()
      case Right(user) => f(user)
    }
  }

  def fetchUser(req: Request[IO]): IO[Either[ErrorCode, User]] = {
    readUserId(req) match {
      case Left(error) => IO.pure(Left(ErrorCode.AuthenticationError))
      case Right(userId) =>
        UserDAO.findById(userId).transact(xa).map {
          case None => Left(ErrorCode.AuthenticationError)
          case Some(user) => Right(user)
        }
    }
  }

  def readUserId(req: Request[IO]): Either[ErrorCode, UserId] = {
    for {
      cookies <- headers.Cookie.from(req.headers).toRight(ErrorCode.ParseError("Cookie parsing error"))
      cookie <- {
        cookies.values.toList
          .find(_.name == World.config.authConfig.cookieName)
          .toRight(ErrorCode.ParseError("Couldn't find the authcookie"))
      }
      token <- {
        JwtCirce.decodeJson(
          cookie.content,
          World.config.authConfig.key,
          List(World.config.authConfig.algo)
        ).toEither.leftMap(_ => ErrorCode.ParseError("Couldnt read JWT"))
      }
      authUserId <- {
        token.hcursor.get[Long]("authUserId")
          .leftMap(_ => ErrorCode.ParseError("Couldnt read authUserId from JWT"))
      }
    } yield {
      authUserId
    }
  }

  def login(username: String, password: String): IO[Response[IO]] = {
    UserDAO.findByName(username).transact(xa).flatMap {
      case Some(user) =>
        if (BCrypt.checkpw(password, user.password)) {
          val res = mkUserLoginResponse(user.id)
          IO.pure(res)
        } else {
          Forbidden()
        }
      case None => Forbidden()
    }
  }

  def logout(): IO[Response[IO]] = {
    IO.pure(mkUserLogoutResponse())
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

    Response(Status.Ok).addCookie(cookie)
  }

  def mkUserLogoutResponse(): Response[IO] = {
    val cookie = ResponseCookie(
      World.config.authConfig.cookieName,
      "",
      path = Some("/"),
      httpOnly = true
    )

    Response(Status.Ok).removeCookie(cookie)
  }

}
