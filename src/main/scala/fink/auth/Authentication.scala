package fink.auth

import java.time.Instant
import java.time.temporal.ChronoUnit

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.World
import fink.World._
import fink.data.JsonInstances._
import fink.data._
import fink.db.UserDAO
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.io._
import org.mindrot.jbcrypt.BCrypt
import pdi.jwt.JwtCirce

object Authentication {

  def authenticate(req: Request[IO]): IO[UserClaims] = {
    readUserClaims(req) match {
      case Left(error) => IO.raiseError(ErrorCode.NotAuthenticated)
      case Right(userClaims) => IO.pure(userClaims)
    }
  }

  def authenticateUser(req: Request[IO]): IO[User] = {
    fetchUser(req).flatMap {
      case Left(e) => IO.raiseError(ErrorCode.NotAuthenticated)
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
    readUserClaims(req) match {
      case Left(error) => IO.pure(Left(ErrorCode.NotAuthenticated))
      case Right(userClaims) =>
        UserDAO.findById(userClaims.userId).transact(xa).map {
          case None => Left(ErrorCode.NotAuthenticated)
          case Some(user) => Right(user)
        }
    }
  }

  def readUserClaims(req: Request[IO]): Either[ErrorCode, UserClaims] = {
    for {
      cookie <- {
        req.cookies.find(_.name == World.config.authConfig.cookieName)
          .toRight(ErrorCode.ParseError("Could not find the authcookie"))
      }
      token <- {
        JwtCirce.decodeJson(
          cookie.content,
          World.config.authConfig.key,
          List(World.config.authConfig.algo)
        ).toEither.leftMap(_ => ErrorCode.ParseError("Could not read JWT"))
      }
      userClaims <- {
        token.as[UserClaims]
          .leftMap(_ => ErrorCode.ParseError("Could not read user claims from JWT"))
      }
    } yield {
      userClaims
    }
  }

  def login(username: String, password: String): IO[Response[IO]] = {
    UserDAO.findByName(username).transact(xa).flatMap {
      case Some(user) =>
        if (BCrypt.checkpw(password, user.password)) {
          val res = mkUserLoginResponse(user)
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

  def mkUserLoginResponse(user: User): Response[IO] = {
    val claims = UserClaims(user.id)
    val claimsJson = claims.asJson

    val jwt = JwtCirce.encode(
      claimsJson,
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
