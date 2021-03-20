package fink.media

import cats.effect.IO
import fink.data.ErrorCode
import org.http4s.MediaType
import org.log4s.{Logger, getLogger}
import org.slf4j.LoggerFactory

import scala.util.Try

// url encoded data, e.g. image uploads
// data:image/png;base64,iVBORw0KGgoAA... -> (image/png, Array(...))
object UrlData {

  case class Item(contentType: MediaType, bytes: Array[Byte])

  val logger: Logger = getLogger("UrlData")

  def parseParts(str: String): Option[(MediaType, String)] = {

    val i = str.indexOf(";base64,")
    if (i == -1 || !str.startsWith("data:")) None
    else {
      val mediaTypeStr = str.substring("data:".size, i)
      val data = str.substring(i + ";base64,".size)
      val mediaType = ContentTypes.mediaTypeFromString(mediaTypeStr)

      Some((mediaType, data))
    }

  }

  def parseItem(str: String): IO[Item] = {

    logger.info(s"Decoding url data ${str.size}")

    for {
      x <- IO.fromEither(parseParts(str).toRight(ErrorCode.InvalidRequest))
      (contentType, data) = x
      bytes <- {
        IO.delay(java.util.Base64.getDecoder.decode(data))
          .handleErrorWith(err => {
            logger.error(err)("Error decoding url data")
            IO.raiseError(ErrorCode.InvalidRequest)
          })
      }
      realContentType = ContentTypes.detectContentType(bytes)
    } yield Item(realContentType, bytes)

  }

}
