package fink.data

sealed trait Notification

object Notification {

  case class CreatedPost(post: Post, author: User) extends Notification
  case class CreatedPage(id: Long) extends Notification
  case class CreatedImage(id: Long) extends Notification
  case class CreatedGallery(id: Long) extends Notification

}
