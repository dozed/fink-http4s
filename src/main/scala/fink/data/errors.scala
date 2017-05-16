package fink.data

sealed trait Notification

object Notification {

  case class CreatedPost(id: Long) extends Notification
  case class CreatedPage(id: Long) extends Notification
  case class CreatedImage(id: Long) extends Notification
  case class CreatedGallery(id: Long) extends Notification

}



sealed trait ErrorCode

object ErrorCode {

  object AlreadyExists extends ErrorCode
  case class NotFound(message: String) extends ErrorCode

}
