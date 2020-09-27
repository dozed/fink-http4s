package fink.web

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.EntityEncoders._
import fink.World._
import fink.data.{Operation, _}
import fink.db.PostDAO
import fink.modules.AuthModule._
import org.http4s._
import org.http4s.dsl.io._

object PostApi {

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "posts" =>

      PostDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case req@POST -> Root / "posts" =>

      for {
        op <- req.as[Operation.CreatePost]
        user <- fetchUser(req).rethrow
        postInfo <- PostDAO.create(op.title, op.text, user, op.tags).transact(xa)
        res <- {
          val msg = Notification.CreatedPost(postInfo)
          Ok(msg)
        }
      } yield {
        res
      }

    case GET -> Root / "posts" / LongVar(postId)  =>

      PostDAO.findPostInfoById(postId).transact(xa).flatMap { infoMaybe =>
        infoMaybe.fold(NotFound())(info => Ok(info))
      }

    case req@POST -> Root / "posts" / LongVar(postId) =>

      for {
        op <- req.as[Operation.UpdatePost]
        user <- fetchUser(req).rethrow
        postInfo <- PostDAO.update(op.id, op.title, op.text, op.shortlink, op.tags).transact(xa)
        res <- {
          val msg = Notification.UpdatedPost(postInfo)
          Ok(msg)
        }
      } yield {
        res
      }

    case req@DELETE -> Root / "posts" / LongVar(postId) =>

      for {
        op <- req.as[Operation.DeletePost]
        user <- fetchUser(req).rethrow
        _ <- PostDAO.delete(op.id).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@POST -> Root / "posts" / LongVar(postId) / "tags" / tagName =>

      for {
        user <- fetchUser(req).rethrow
        _ <- PostDAO.addTag(postId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@DELETE -> Root / "posts" / LongVar(postId) / "tags" / tagName =>

      for {
        user <- fetchUser(req).rethrow
        _ <- PostDAO.removeTag(postId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }

}
