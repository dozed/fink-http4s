package fink.data


sealed trait ErrorCode extends Throwable {
  override def getMessage() = this.getClass.getSimpleName.replaceAllLiterally("$", "")
}

object ErrorCode {

  trait ErrorCodeMsg extends ErrorCode {
    def msg: String
    override def getMessage: String = msg
  }

  object AlreadyExists extends ErrorCode
  case class NotFound(msg: String) extends ErrorCodeMsg
  case class ParseError(msg: String) extends ErrorCodeMsg

}
