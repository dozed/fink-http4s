package fink.web

import cats.effect._
import cats.implicits._
import doobie.implicits._
import fink.EntityEncoders._
import fink.World._
import fink.data.JsonInstances._
import fink.data._
import fink.db.{GalleryDAO, ImageDAO}
import fink.media.{Uploads, UrlData}
import fink.auth.Authorization
import fink.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object GalleryApi {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "galleries" =>

      GalleryDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case req@POST -> Root / "galleries" =>

      for {
        op <- req.decodeJson[Operation.CreateGallery]
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        galleryInfo <- GalleryDAO.create(op.title, op.text, user, op.tags).transact(xa)
        res <- {
          val msg = Notification.CreatedGallery(galleryInfo)
          Ok(msg)
        }
      } yield {
        res
      }

    case GET -> Root / "galleries" / LongVar(pageId)  =>

      GalleryDAO.findGalleryInfoById(pageId).transact(xa).flatMap { infoMaybe =>
        infoMaybe.fold(NotFound())(info => Ok(info))
      }

    case req@POST -> Root / "galleries" / LongVar(imageId) =>

      for {
        op <- req.decodeJson[Operation.UpdateGallery]
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        galleryInfo <- GalleryDAO.update(op.id, op.title, op.text, op.shortlink, op.tags).transact(xa)
        res <- {
          val msg = Notification.UpdatedGallery(galleryInfo)
          Ok(msg)
        }
      } yield {
        res
      }

    case req@DELETE -> Root / "galleries" / LongVar(galleryId) =>

      for {
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        _ <- GalleryDAO.delete(galleryId).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@POST -> Root / "galleries" / LongVar(galleryId) / "images" =>

      for {
        op <- req.decodeJson[Operation.UploadImageToGallery]
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)

        imageData <- UrlData.parseItem(op.imageData)
        uploadImage <- Uploads.storeUrlDataItem(config, imageData)
        imageInfo <- {
          ImageDAO.create(op.title, uploadImage.hash, uploadImage.extension, uploadImage.contentType, uploadImage.fileName, user)
            .flatMap(imageInfo => GalleryDAO.appendImage(op.galleryId, imageInfo.image.id).map(_ => imageInfo))
            .transact(xa)
        }

        res <- Ok(imageInfo)

      } yield {
        res
      }

    case req@POST -> Root / "galleries" / LongVar(galleryId) / "sorting" =>

      for {
        op <- req.decodeJson[Operation.SortImage]
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        _ <- GalleryDAO.sortImage(op.galleryId, op.from, op.to).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@POST -> Root / "galleries" / LongVar(galleryId) / "tags" / tagName =>

      for {
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        _ <- GalleryDAO.addTag(galleryId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@DELETE -> Root / "galleries" / LongVar(galleryId) / "tags" / tagName =>

      for {
        user <- req.authenticateUser
        _ <- Authorization.authorizeEdit(user)
        _ <- GalleryDAO.removeTag(galleryId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }


}
