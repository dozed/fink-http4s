package fink

import cats.effect._
import fink.data.JsonInstances._
import fink.data._
import org.http4s._
import org.http4s.circe._

object EntityEncoders {

  implicit val createdPostEntityEncoder: EntityEncoder[IO, Notification.CreatedPost] = jsonEncoderOf[IO, Notification.CreatedPost]
  implicit val updatedPostEntityEncoder: EntityEncoder[IO, Notification.UpdatedPost] = jsonEncoderOf[IO, Notification.UpdatedPost]
  implicit val postInfoEntityEncoder: EntityEncoder[IO, PostInfo] = jsonEncoderOf[IO, PostInfo]
  implicit val postsEntityEncoder: EntityEncoder[IO, List[Post]] = jsonEncoderOf[IO, List[Post]]

  implicit val createdPageEntityEncoder: EntityEncoder[IO, Notification.CreatedPage] = jsonEncoderOf[IO, Notification.CreatedPage]
  implicit val updatedPageEntityEncoder: EntityEncoder[IO, Notification.UpdatedPage] = jsonEncoderOf[IO, Notification.UpdatedPage]
  implicit val pageInfoEntityEncoder: EntityEncoder[IO, PageInfo] = jsonEncoderOf[IO, PageInfo]
  implicit val pagesEntityEncoder: EntityEncoder[IO, List[Page]] = jsonEncoderOf[IO, List[Page]]

  implicit val createdGalleryEntityEncoder: EntityEncoder[IO, Notification.CreatedGallery] = jsonEncoderOf[IO, Notification.CreatedGallery]
  implicit val updatedGalleryEntityEncoder: EntityEncoder[IO, Notification.UpdatedGallery] = jsonEncoderOf[IO, Notification.UpdatedGallery]
  implicit val galleryInfoEntityEncoder: EntityEncoder[IO, GalleryInfo] = jsonEncoderOf[IO, GalleryInfo]
  implicit val galleryEntityEncoder: EntityEncoder[IO, List[Gallery]] = jsonEncoderOf[IO, List[Gallery]]

  implicit val createdImageEntityEncoder: EntityEncoder[IO, Notification.CreatedImage] = jsonEncoderOf[IO, Notification.CreatedImage]
  implicit val updatedImageEntityEncoder: EntityEncoder[IO, Notification.UpdatedImage] = jsonEncoderOf[IO, Notification.UpdatedImage]
  implicit val imageInfoEntityEncoder: EntityEncoder[IO, ImageInfo] = jsonEncoderOf[IO, ImageInfo]
  implicit val imageEntityEncoder: EntityEncoder[IO, List[Image]] = jsonEncoderOf[IO, List[Image]]

  implicit val tagsEntityEncoder: EntityEncoder[IO, List[Tag]] = jsonEncoderOf[IO, List[Tag]]

}
