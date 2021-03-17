package fink.media

import fink.data.{AppConfig, ErrorCode}
import java.nio.file.Files

import cats.syntax.all._
import cats.effect._
import org.http4s.MediaType
import org.log4s.{Logger, getLogger}

object Uploads {

  case class Upload(hash: String, extension: String, fileName: String, contentType: MediaType)

  val logger: Logger = getLogger("Uploads")

  def detectExtension(contentType: MediaType): IO[String] = {
    ContentTypes.contentTypeExtensions.get(contentType) match {
      case Some(ext) => IO.pure(ext)
      case None =>
        IO.delay(logger.error(ErrorCode.InvalidRequest)(s"Content type not supported: ${contentType.show}")) >>
          IO.raiseError(ErrorCode.InvalidRequest)
    }
  }

  def storeUrlDataItem(config: AppConfig, item: UrlData.Item): IO[Upload] = {
    for {
      _ <- IO.unit
      hash = Hashes.md5(s"${System.currentTimeMillis()}-${Thread.currentThread().getId}")
      ext <- detectExtension(item.contentType)
      filename = s"$hash.$ext"
      uploadFile = StaticFiles.mkUploadFile(config, filename)
      _ <- {
        IO.delay(logger.info(s"uploading file to ${uploadFile.getPath} with content type ${item.contentType.show} size ${item.bytes.length}"))
      }
      _ <- {
        IO.delay(Files.write(uploadFile.toPath, item.bytes))
          .recoverWith { err =>
            logger.error(err)("Error writing file")
            IO.raiseError(ErrorCode.InvalidRequest)
          }
      }
    } yield {
      Upload(hash, ext, filename, item.contentType)
    }
  }

  def deleteIfExists(config: AppConfig, uploadName: String): Unit = {
    val file = StaticFiles.mkUploadFile(config, uploadName)
    if (file.exists) file.delete()
  }

}
