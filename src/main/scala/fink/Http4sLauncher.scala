package fink

import java.io.File

import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.staticcontent._
import org.http4s.multipart.Multipart
import org.http4s.EntityDecoder.multipart
import fs2._
import fs2.text._
import _root_.io.circe.Json

import scala.concurrent.ExecutionContext
import cats.implicits._
import cats.effect._
import doobie.implicits._
import doobie._
import pdi.jwt.{JwtAlgorithm, JwtCirce}
import fink.data._
import fink.data.JsonInstances._
import fink.data.Operation
import fink.db.{GalleryDAO, ImageDAO, PageDAO, PostDAO, TagDAO, UserDAO, xa}
import fink.media.{Hashes, Uploads, UrlData}

object Http4sLauncher extends App {


  implicit val createPostEntityDecoder: EntityDecoder[IO, Operation.CreatePost] = jsonOf[IO, Operation.CreatePost]
  implicit val updatePostEntityDecoder: EntityDecoder[IO, Operation.UpdatePost] = jsonOf[IO, Operation.UpdatePost]
  implicit val deletePostEntityDecoder: EntityDecoder[IO, Operation.DeletePost] = jsonOf[IO, Operation.DeletePost]
  implicit val createdPostEntityEncoder: EntityEncoder[IO, Notification.CreatedPost] = jsonEncoderOf[IO, Notification.CreatedPost]
  implicit val updatedPostEntityEncoder: EntityEncoder[IO, Notification.UpdatedPost] = jsonEncoderOf[IO, Notification.UpdatedPost]
  implicit val postInfoEntityEncoder: EntityEncoder[IO, PostInfo] = jsonEncoderOf[IO, PostInfo]
  implicit val postsEntityEncoder: EntityEncoder[IO, List[Post]] = jsonEncoderOf[IO, List[Post]]

  implicit val createPageEntityDecoder: EntityDecoder[IO, Operation.CreatePage] = jsonOf[IO, Operation.CreatePage]
  implicit val updatePageEntityDecoder: EntityDecoder[IO, Operation.UpdatePage] = jsonOf[IO, Operation.UpdatePage]
  implicit val deletePageEntityDecoder: EntityDecoder[IO, Operation.DeletePage] = jsonOf[IO, Operation.DeletePage]
  implicit val createdPageEntityEncoder: EntityEncoder[IO, Notification.CreatedPage] = jsonEncoderOf[IO, Notification.CreatedPage]
  implicit val updatedPageEntityEncoder: EntityEncoder[IO, Notification.UpdatedPage] = jsonEncoderOf[IO, Notification.UpdatedPage]
  implicit val pageInfoEntityEncoder: EntityEncoder[IO, PageInfo] = jsonEncoderOf[IO, PageInfo]
  implicit val pagesEntityEncoder: EntityEncoder[IO, List[Page]] = jsonEncoderOf[IO, List[Page]]

  implicit val createGalleryEntityDecoder: EntityDecoder[IO, Operation.CreateGallery] = jsonOf[IO, Operation.CreateGallery]
  implicit val updateGalleryEntityDecoder: EntityDecoder[IO, Operation.UpdateGallery] = jsonOf[IO, Operation.UpdateGallery]
  implicit val deleteGalleryEntityDecoder: EntityDecoder[IO, Operation.DeleteGallery] = jsonOf[IO, Operation.DeleteGallery]
  implicit val createdGalleryEntityEncoder: EntityEncoder[IO, Notification.CreatedGallery] = jsonEncoderOf[IO, Notification.CreatedGallery]
  implicit val updatedGalleryEntityEncoder: EntityEncoder[IO, Notification.UpdatedGallery] = jsonEncoderOf[IO, Notification.UpdatedGallery]
  implicit val uploadImageToGalleryEntityDecoder: EntityDecoder[IO, Operation.UploadImageToGallery] = jsonOf[IO, Operation.UploadImageToGallery]
  implicit val removeImageFromGalleryEntityDecoder: EntityDecoder[IO, Operation.RemoveImageFromGallery] = jsonOf[IO, Operation.RemoveImageFromGallery]
  implicit val galleryInfoEntityEncoder: EntityEncoder[IO, GalleryInfo] = jsonEncoderOf[IO, GalleryInfo]
  implicit val galleryEntityEncoder: EntityEncoder[IO, List[Gallery]] = jsonEncoderOf[IO, List[Gallery]]

  implicit val createImageEntityDecoder: EntityDecoder[IO, Operation.CreateImage] = jsonOf[IO, Operation.CreateImage]
  implicit val updateImageEntityDecoder: EntityDecoder[IO, Operation.UpdateImage] = jsonOf[IO, Operation.UpdateImage]
  implicit val deleteImageEntityDecoder: EntityDecoder[IO, Operation.DeleteImage] = jsonOf[IO, Operation.DeleteImage]
  implicit val createdImageEntityEncoder: EntityEncoder[IO, Notification.CreatedImage] = jsonEncoderOf[IO, Notification.CreatedImage]
  implicit val updatedImageEntityEncoder: EntityEncoder[IO, Notification.UpdatedImage] = jsonEncoderOf[IO, Notification.UpdatedImage]
  implicit val imageInfoEntityEncoder: EntityEncoder[IO, ImageInfo] = jsonEncoderOf[IO, ImageInfo]
  implicit val imageEntityEncoder: EntityEncoder[IO, List[Image]] = jsonEncoderOf[IO, List[Image]]

  implicit val tagsEntityEncoder: EntityEncoder[IO, List[Tag]] = jsonEncoderOf[IO, List[Tag]]

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)


  val config = AppConfig.load()

  // val author = User(1, "foo", "bar")
  // val publicUser = User(-1, "foo", "bar")

  val key = "secretK3y"
  val algo = JwtAlgorithm.HS256

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



  val adminService = HttpRoutes.of[IO] {

    case req @ GET -> Root / "index.html" =>
      StaticFile.fromFile(new File("index.html"), ExecutionContext.global, Some(req))
        .getOrElseF(NotFound())

    case GET -> Root / "login" =>

      val jwt = JwtCirce.encode(Json.obj("authUserId" -> Json.fromLong(1)), key, JwtAlgorithm.HS256)

      Ok().map(_.addCookie("ui", jwt))

    case GET -> Root / "admin" / "index" =>
      Ok(s"Hello, name.")

    case GET -> Root / "admin" / "posts" =>
      Ok(s"Hello, name.")
  }


  val postApiService = HttpRoutes.of[IO] {
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

  val pageApiService = HttpRoutes.of[IO] {
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

  val galleryApiService = HttpRoutes.of[IO] {
    case GET -> Root / "galleries" =>

      GalleryDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case req@POST -> Root / "galleries" =>

      for {
        op <- req.as[Operation.CreateGallery]
        user <- fetchUser(req).rethrow
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
        op <- req.as[Operation.UpdateGallery]
        user <- fetchUser(req).rethrow
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
        op <- req.as[Operation.DeleteGallery]
        user <- fetchUser(req).rethrow
        _ <- GalleryDAO.delete(op.id).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@POST -> Root / "galleries" / LongVar(galleryId) / "images" =>

      for {
        op <- req.as[Operation.UploadImageToGallery]
        user <- fetchUser(req).rethrow

        image <- UrlData.decodeFile(op.imageData)
        hash = Hashes.md5(s"${System.currentTimeMillis()}-${Thread.currentThread().getId}")
        uploadFile <- Uploads.store(config, image, hash)
        imageInfo <- {
          ImageDAO.create(op.title, hash, image.contentType.show, uploadFile.getPath, user)
            .flatMap(imageInfo => GalleryDAO.addImage(op.galleryId, imageInfo.image.id).map(_ => imageInfo))
            .transact(xa)
        }

        res <- Ok(imageInfo)

      } yield {
        res
      }

    case req@POST -> Root / "galleries" / LongVar(galleryId) / "tags" / tagName =>

      for {
        user <- fetchUser(req).rethrow
        _ <- GalleryDAO.addTag(galleryId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

    case req@DELETE -> Root / "galleries" / LongVar(galleryId) / "tags" / tagName =>

      for {
        user <- fetchUser(req).rethrow
        _ <- GalleryDAO.removeTag(galleryId, tagName).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }

  val imageApiService = HttpRoutes.of[IO] {
    case GET -> Root / "images" =>

      ImageDAO.findAll.transact(xa).flatMap { xs =>
        Ok(xs)
      }

    case req@POST -> Root / "images" =>

      for {
        op <- req.as[Operation.CreateImage]
        user <- fetchUser(req).rethrow

        image <- UrlData.decodeFile(op.imageData)
        hash = Hashes.md5(s"${System.currentTimeMillis()}-${Thread.currentThread().getId}")
        uploadFile <- Uploads.store(config, image, hash)
        imageInfo <- ImageDAO.create(op.title, hash, image.contentType.show, uploadFile.getPath, user).transact(xa)

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
//        op <- req.as[Operation.UpdateImage]
//        user <- fetchUser(req).rethrow
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
        op <- req.as[Operation.DeleteImage]
        user <- fetchUser(req).rethrow
        _ <- ImageDAO.delete(op.id).transact(xa)
        res <- Ok()
      } yield {
        res
      }

  }


  val httpApp = Router(
    "/" -> adminService,
    "/api" -> postApiService,
    "/api" -> pageApiService,
    "/api" -> galleryApiService,
    "/api" -> imageApiService,
    "/assets" -> fileService[IO](FileService.Config("./assets")),
  ).orNotFound

  val serverBuilder = BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)

  serverBuilder.serve.compile.drain.as(ExitCode.Success).unsafeRunSync()


}
