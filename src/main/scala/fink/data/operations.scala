package fink.data

trait Operation

object Operation {

  case class CreatePost(
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  )

  case class UpdatePost(
    id: Long,
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  )

  case class DeletePost(
    id: Long,
  )

  case class CreatePage(
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  )

  case class UpdatePage(
    id: Long,
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  )

  case class DeletePage(
    id: Long,
  )

  case class CreateGallery(
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  )

  case class UpdateGallery(
    id: Long,
    title: String,
    text: String,
    shortlink: String,
    tags: List[String]
  )

  case class DeleteGallery(
    id: Long,
  )

  case class UploadImageToGallery(
    galleryId: Long,
    title: String,
    imageData: String
  )

  case class RemoveImageFromGallery(
    galleryId: Long,
    imageId: Long,
  )

  case class CreateImage(
    title: String,
    imageData: String,
  )

  case class UpdateImage(
    id: Long,
    title: String,
  )

  case class DeleteImage(
    id: Long,
  )

}
