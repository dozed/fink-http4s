package fink.web

import cats.effect._
import doobie.implicits._
import fink.EntityEncoders._
import fink.World._
import fink.data.JsonInstances._
import fink.data._
import fink.db.PageDAO
import fink.modules.AuthModule._
import fink.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object PageApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "pages" =>

      PageDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case GET -> Root / "pages" / LongVar(pageId)  =>

      PageDAO.findPageInfoById(pageId).transact(xa).flatMap { infoMaybe =>
        infoMaybe.fold(NotFound())(info => Ok(info))
      }

    case req@POST -> Root / "pages" =>

      loadUser(req) { user =>

        for {
          op <- req.decodeJson[Operation.CreatePage]
          pageInfo <- PageDAO.create(op.title, op.text, user, op.tags).transact(xa)
          res <- {
            val msg = Notification.CreatedPage(pageInfo)
            Ok(msg)
          }
        } yield {
          res
        }

      }

    case req@POST -> Root / "pages" / LongVar(pageId) =>

      for {
        op <- req.decodeJson[Operation.UpdatePage]
        user <- req.authenticateUser
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
        op <- req.decodeJson[Operation.DeletePage]
        user <- req.authenticateUser
        _ <- PageDAO.delete(op.id).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@POST -> Root / "pages" / LongVar(pageId) / "tags" / tagName =>

      for {
        user <- req.authenticateUser
        _ <- PageDAO.addTag(pageId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@DELETE -> Root / "pages" / LongVar(pageId) / "tags" / tagName =>

      for {
        user <- req.authenticateUser
        _ <- PageDAO.removeTag(pageId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }

}
