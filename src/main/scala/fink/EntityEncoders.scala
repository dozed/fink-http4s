package fink

import cats.effect._
import fink.World._
import fink.data.JsonInstances._
import fink.data.{Operation, _}
import org.http4s._
import org.http4s.circe._

object EntityEncoders {

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

  implicit val loginEntityDecoder: EntityDecoder[IO, Operation.Login] = jsonOf[IO, Operation.Login]

}
