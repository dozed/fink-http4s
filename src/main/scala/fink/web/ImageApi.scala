package fink.web

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.EntityEncoders._
import fink.World._
import fink.data.JsonInstances._
import fink.data.Operation
import fink.db.ImageDAO
import fink.media.{Hashes, Uploads, UrlData}
import fink.auth.Authorization
import fink.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object ImageApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "images" =>

      ImageDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case req@POST -> Root / "images" =>

      for {
        op <- req.decodeJson[Operation.CreateImage]
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)

        image <- UrlData.parseItem(op.imageData)
        hash = Hashes.md5(s"${System.currentTimeMillis()}-${Thread.currentThread().getId}")
        uploadFile <- Uploads.store(config, image, hash)
        imageInfo <- ImageDAO.create(op.title, hash, image.contentType.show, uploadFile.getName, user).transact(xa)

        res <- Ok(imageInfo)

      } yield {
        res
      }

    case GET -> Root / "images" / LongVar(imageId)  =>

      ImageDAO.findImageInfoById(imageId).transact(xa).flatMap { infoMaybe =>
        infoMaybe.fold(NotFound())(info => Ok(info))
      }

    //    case req@POST -> Root / "images" / LongVar(imageId) =>
    //
    //      for {
    //        op <- req.decodeJson[Operation.UpdateImage]
    //        user <- req.authenticateUser
    //        imageInfo <- ImageDAO.update(op.id, op.title, op.text, op.shortlink, op.tags).transact(xa)
    //        res <- {
    //          val msg = Notification.UpdatedImage(imageInfo)
    //          Ok(msg)
    //        }
    //      } yield {
    //        res
    //      }

    case req@DELETE -> Root / "images" / LongVar(imageId) =>

      for {
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        _ <- ImageDAO.delete(imageId).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }

}
