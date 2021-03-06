package fink.data


sealed trait ErrorCode extends Throwable {
  override def getMessage(): String = this.getClass.getSimpleName.replace("$", "")
}

object ErrorCode {

  trait ErrorCodeMsg extends ErrorCode {
    def msg: String
    override def getMessage(): String = msg
  }

  case object NotAuthenticated extends ErrorCode
  case object NotAuthorized extends ErrorCode

  case object CouldNotFindAuthCookie extends ErrorCode
  case object CouldNotReadJwtFromAuthCookie extends ErrorCode
  case object CouldNotReadUserClaimsFromAuthCookie extends ErrorCode

  object AlreadyExists extends ErrorCode
  case class UserNotFound(msg: String) extends ErrorCodeMsg
  case class ParseError(msg: String) extends ErrorCodeMsg

  case object InvalidRequest extends ErrorCode

}
