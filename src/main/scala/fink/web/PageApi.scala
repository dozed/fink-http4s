package fink.web

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.EntityEncoders._
import fink.World._
import fink.data.{Operation, _}
import fink.db.{PageDAO, TagDAO}
import fink.modules.UserModule._
import org.http4s._
import org.http4s.dsl.io._

object PageApi {

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "pages" =>

      PageDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case req@POST -> Root / "pages" =>

      for {
        op <- req.as[Operation.CreatePage]
        user <- fetchUser(req).rethrow
        postInfo <- PageDAO.create(op.title, op.text, user, op.tags).transact(xa)
        res <- {
          val msg = Notification.CreatedPage(postInfo)
          Ok(msg)
        }
      } yield {
        res
      }

    case GET -> Root / "pages" / LongVar(pageId)  =>

      PageDAO.findPageInfoById(pageId).transact(xa).flatMap { infoMaybe =>
        infoMaybe.fold(NotFound())(info => Ok(info))
      }

    case req@POST -> Root / "pages" / LongVar(postId) =>

      for {
        op <- req.as[Operation.UpdatePage]
        user <- fetchUser(req).rethrow
        pageInfo <- PageDAO.update(op.id, op.title, op.text, op.shortlink, op.tags).transact(xa)
        res <- {
          val msg = Notification.UpdatedPage(pageInfo)
          Ok(msg)
        }
      } yield {
        res
      }

    case req@DELETE -> Root / "pages" / LongVar(pageId) =>

      for {
        op <- req.as[Operation.DeletePage]
        user <- fetchUser(req).rethrow
        _ <- PageDAO.delete(op.id).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@POST -> Root / "pages" / LongVar(pageId) / "tags" / tagName =>

      for {
        user <- fetchUser(req).rethrow
        _ <- PageDAO.addTag(pageId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@DELETE -> Root / "pages" / LongVar(pageId) / "tags" / tagName =>

      for {
        user <- fetchUser(req).rethrow
        _ <- PageDAO.removeTag(pageId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }

  val tagApiService = HttpRoutes.of[IO] {
    case GET -> Root / "tags" =>

      TagDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

  }

}
