package fink.modules

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.World._
import fink.data._
import fink.db.UserDAO
import org.http4s._
import pdi.jwt.{JwtAlgorithm, JwtCirce}

object UserModule {

  def readUserId(req: Request[IO]): Either[ErrorCode, UserId] = {
    for {
      header <- headers.Cookie.from(req.headers).toRight(ErrorCode.ParseError("Cookie parsing error"))
      cookie <- header.values.toList.find(_.name == "ui").toRight(ErrorCode.ParseError("Couldn't find the authcookie"))
      token <- JwtCirce.decodeJson(cookie.content, key, Seq(JwtAlgorithm.HS256)).toEither.leftMap(_ => ErrorCode.ParseError("Couldnt read JWT"))
      authUserId <- token.hcursor.downField("authUserId").as[Long].leftMap(_ => ErrorCode.ParseError("Couldnt read authUserId from JWT"))
    } yield {
      authUserId
    }
  }

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


}
