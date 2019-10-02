package fink.data


sealed trait ErrorCode

object ErrorCode {

  object AlreadyExists extends ErrorCode
  case class NotFound(message: String) extends ErrorCode

}
