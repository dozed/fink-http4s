package fink.data

sealed trait Notification

object Notification {

  case class CreatedPost(postInfo: PostInfo) extends Notification
  case class UpdatedPost(postInfo: PostInfo) extends Notification
  case class CreatedPage(pageInfo: PageInfo) extends Notification
  case class UpdatedPage(pageInfo: PageInfo) extends Notification
  case class CreatedGallery(galleryInfo: GalleryInfo) extends Notification
  case class UpdatedGallery(galleryInfo: GalleryInfo) extends Notification
  case class CreatedImage(id: Long) extends Notification

}
