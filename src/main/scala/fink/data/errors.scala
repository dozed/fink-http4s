package fink.data


sealed trait ErrorCode extends Throwable {
  override def getMessage() = this.getClass.getSimpleName.replace("$", "")
}

object ErrorCode {

  trait ErrorCodeMsg extends ErrorCode {
    def msg: String
    override def getMessage: String = msg
  }

  case object NotAuthenticated extends ErrorCode
  case object NotAuthorized extends ErrorCode

  object AlreadyExists extends ErrorCode
  case class UserNotFound(msg: String) extends ErrorCodeMsg
  case class ParseError(msg: String) extends ErrorCodeMsg

  case object InvalidRequest extends ErrorCode

}
