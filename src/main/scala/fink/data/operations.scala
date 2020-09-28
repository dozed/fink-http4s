package fink.data

sealed trait Operation

object Operation {

  case class CreatePost(
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  ) extends Operation

  case class UpdatePost(
    id: Long,
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  ) extends Operation

  case class DeletePost(
    id: Long,
  ) extends Operation

  case class CreatePage(
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  ) extends Operation

  case class UpdatePage(
    id: Long,
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  ) extends Operation

  case class DeletePage(
    id: Long,
  ) extends Operation

  case class CreateGallery(
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  ) extends Operation

  case class UpdateGallery(
    id: Long,
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  ) extends Operation

  case class DeleteGallery(
    id: Long,
  ) extends Operation

  case class UploadImageToGallery(
    galleryId: Long,
    title: String,
    imageData: String
  ) extends Operation

  case class RemoveImageFromGallery(
    galleryId: Long,
    imageId: Long,
  ) extends Operation

  case class CreateImage(
    title: String,
    imageData: String,
  ) extends Operation

  case class UpdateImage(
    id: Long,
    title: String,
  ) extends Operation

  case class DeleteImage(
    id: Long,
  ) extends Operation

  case class Login(
    username: String,
    password: String
  ) extends Operation

}
