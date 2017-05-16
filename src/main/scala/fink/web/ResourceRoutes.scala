//package fink.web
//
//import fink.World
//import fink.data.{Tag, _}
//import fink.support._
//import org.scalatra.servlet.FileUploadSupport
//import org.scalatra._
//import org.scalatra.json.{JacksonJsonSupport, JsonResult}
//import slick.driver.H2Driver.api._
//import org.json4s.scalaz.JsonScalaz._
//import drafts.WriteExt._
//import drafts.ReadExt._
//
//import _root_.scalaz._, Scalaz._
//
//import scala.concurrent.Future
//
//trait ResourceRoutes extends ScalatraServlet with FutureSupport with FileUploadSupport with JacksonJsonSupport {
//
//  // lift several types to EitherT[Future, E, A]
//
//  implicit class OptionExt[A](o: Option[A]) {
//    def orF[E](e: => E) = EitherT(Future.successful(o \/> e))
//  }
//
//  implicit class EitherExt[E, A](a: E \/ A) {
//    // def eitherT = EitherT(a)
//    def leftAsF[E2](e2: => E2) = EitherT(Future.successful(a.leftAs(e2)))
//  }
//
//  implicit class FutureExt[A](a: Future[A]) {
//    // where A is not an E \/ A
//    def rightT[E] = EitherT(a.map(_.right[E]))
//    def leftAsT[E, B](e: => E)(implicit ev: A <:< (E \/ B)) = EitherT(a.map(x => ev(x).leftAs(e)))
//    def \/>[E, B](e: => E)(implicit ev: A <:< Option[B]) = EitherT(a.map(x => ev(x) \/> e))
//  }
//
//
//  import Queries._
//
//  def world: World
//
//  get("/api/settings") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        world.db.run(Queries.settings.result.headOption).map {
//          case Some(s) => Ok(JsonResult(s.toJson))
//          case None => InternalServerError()
//        }
//      }
//    }
//  }
//
//  put("/api/settings") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        request.body.validate[Settings].fold(
//          _ => Future.successful(BadRequest()),
//          s => world.db.run(settings.update(s))
//        )
//      }
//    }
//  }
//
//  get("/api/pages") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        world.db.run(pages.result).map(_.toList.toJson)
//      }
//    }
//  }
//
//  post("/api/pages") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          page <- request.body.read[Page] leftAsF BadRequest
//          id <- world.db.run((pages returning pages.map(_.id)) += page).rightT
//        } yield {
//          Ok(page.copy(id = id))
//        }).run
//      }
//    }
//  }
//
//  get("/api/pages/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <- params.getAs[Long]("id") orF BadRequest()
//          page <- world.db.run(pages.filter(_.id === id).result.headOption) \/> NotFound()
//        } yield {
//          Ok(page.toJson)
//        }).run
//      }
//    }
//  }
//
//  put("/api/pages/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          page <- request.body.read[Page] leftAsF NotFound()
//          _ <- {
//            println(s"updating page $page")
//            world.db.run(pages.update(page)).rightT[ActionResult]
//          }
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
//  delete("/api/pages/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <- params.getAs[Long]("id") orF BadRequest()
//          numDeleted <- world.db.run(pages.filter(_.id === id).delete).rightT
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
//  get("/api/posts") {
//    new AsyncResult {
//      val is = {
//        world.db.run(posts.result)
//      }
//    }
//  }
//
////  post("/api/posts") {
////    val post = read[Post](request.body)
////    val id = postRepository.create(post.date, post.title, post.author, post.text, post.tags.map(_.name), post.category)
////
////    postRepository.byId(id) match {
////      case Some(post) => post
////      case None => halt(500)
////    }
////  }
////
////  get("/api/posts/:id") {
////    val id = params("id").toLong
////
////    postRepository.byId(id) match {
////      case Some(post) => post
////      case None => halt(404)
////    }
////  }
////
////  put("/api/posts/:id") {
////    val post = read[Post](request.body)
////
////    postRepository.update(post) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
////
////  delete("/api/posts/:id") {
////    val id = params("id").toLong
////
////    postRepository.delete(id) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404)
////    }
////  }
////
////  get("/api/posts/:id/tags") {
////    val id = params("id").toLong
////
////    postRepository.byId(id) match {
////      case Some(post) => post.tags
////      case None => halt(404)
////    }
////  }
////
////  post("/api/posts/:id/tags/:name") {
////    val id = params("id").toLong
////    val name = params("name")
////
////    postRepository.addTag(id, name) match {
////      case Ok => halt(204)
////      case AlreadyExists => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
////
////  delete("/api/posts/:id/tags/:name") {
////    val id = params("id").toLong
////    val name = params("name")
////
////    postRepository.removeTag(id, name) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
//
//  get("/api/tags") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        world.db.run(tags.result).map(_.toList.toJson)
//      }
//    }
//  }
//
//  get("/api/tags/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <- params.getAs[Long]("id") orF BadRequest()
//          tag <- world.db.run(tags.filter(_.id === id).result.headOption) \/> NotFound()
//        } yield {
//          Ok(tag.toJson)
//        }).run
//      }
//    }
//  }
//
//  post("/api/tags") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          tag <- EitherT(Future.successful(request.body.read[Tag] leftAs BadRequest))
//          id <- EitherT(world.db.run((tags returning tags.map(_.id)) += tag).map(x => x.right))
//        } yield {
//          Ok(tag.copy(id = id))
//        }).run
//      }
//    }
//  }
//
//  put("/api/tags/:id") {
//   new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          tag <- request.body.read[Tag] leftAsF NotFound()
//          _ <- EitherT {
//            println(s"updating tag $tag")
//            world.db.run(tags.update(tag).map(_.right))
//          }
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
//  delete("/api/tags/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <-params.getAs[Long]("id") orF BadRequest()
//          numDeleted <- EitherT(world.db.run(tags.filter(_.id === id).delete).map(_.right))
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
//  get("/api/galleries") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        world.db.run(galleries.result).map(xs => xs.toList.toJson)
//      }
//    }
//  }
//
//  post("/api/galleries") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          g <- request.body.read[Gallery] leftAs BadRequest()
//        } yield {
//          world.db.run(galleries += g)
//        }).sequence[Future, Int]
//      }
//    }
//  }
//
//  get("/api/galleries/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <- EitherT(Future.successful(params.getAs[Long]("id") \/> BadRequest()))
//          gallery <- EitherT[Future, ActionResult, Gallery] {
//            println(s"loading gallery with id: $id")
//
//            world.db.run(galleries.filter(_.id === id).result.headOption).map {
//              g =>
//                println(s"loaded gallery")
//                println(g)
//                g \/> NotFound()
//            }
//          }
//        } yield {
//          Ok(gallery.toJson)
//        }).run
//      }
//    }
//  }
//
//  put("/api/galleries/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          gallery <- EitherT(Future.successful(request.body.read[Gallery] leftAs NotFound()))
//          _ <- EitherT {
//            println(s"read gallery")
//            println(gallery)
//            world.db.run(galleries.update(gallery).map(_.right))
//          }
//
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
//  delete("/api/galleries/:id") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <- EitherT(Future.successful(params.getAs[Long]("id") \/> BadRequest()))
//          numDeleted <- EitherT(world.db.run(galleries.filter(_.id === id).delete).map(_.right))
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
//
//  post("/api/galleries/:id/cover") {
//    new AsyncResult {
//      override val is: Future[_] = {
//        (for {
//          id <- params.getAs[Long]("id") orF BadRequest()
//          coverId <- params.getAs[Long]("coverId") orF BadRequest()
//          _ <- world.db.run(galleries.filter(_.id === id).map(_.coverId).update(coverId)).rightT
//        } yield {
//          NoContent()
//        }).run
//      }
//    }
//  }
//
////  get("/api/galleries/:id/images") {
////    val id = params("id").toLong
////
////    galleryRepository.byId(id) match {
////      case Some(gallery) => gallery.images
////      case None => halt(404, "Could not find gallery: %s".format(id))
////    }
////  }
////
////  post("/api/galleries/:id/images") {
////    val id = params("id").toLong
////    val order = params("order").split(",").toList.map(_.toLong)
////
////    galleryRepository.updateImageOrder(id, order)
////    halt(204)
////  }
////
////  post("/api/galleries/:galleryId/images/:imageId") {
////    val galleryId = params("galleryId").toLong
////    // val imageId = params("imageId").toLong => 500
////    val imageId = params("imageId").toLong
////
////    galleryRepository.addImage(galleryId, imageId) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
////
////  delete("/api/galleries/:galleryId/images/:imageId") {
////    val galleryId = params("galleryId").toLong
////    val imageId = params("imageId").toLong
////
////    galleryRepository.removeImage(galleryId, imageId) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
////
////  get("/api/images") {
////    imageRepository.findAll
////  }
////
////  post("/api/images") {
////    MediaManager.processUpload(fileParams("file")) match {
////      case Some(ImageUpload(hash, contentType, filename)) =>
////        imageRepository.create(0, filename, "", hash, contentType, filename)
////      case None => halt(500)
////    }
////  }
////
////  get("/api/images/:id") {
////    val id = params("id").toLong
////
////    imageRepository.byId(id) match {
////      case Some(image) => image
////      case None => halt(404, "Could not find image: %s".format(id))
////    }
////  }
////
////  put("/api/images/:id") {
////    val id = params("id").toLong
////    val image = read[Image](request.body)
////
////    imageRepository.update(image) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
////
////  delete("/api/images/:id") {
////    val id = params("id").toLong
////    imageRepository.delete(id) match {
////      case Ok => halt(204)
////      case NotFound(message) => halt(404, message)
////    }
////  }
//
//}
