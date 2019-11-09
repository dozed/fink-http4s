package fink.media

import fink.data.{AppConfig, ErrorCode}

import java.io.File
import java.nio.file.Files
import org.slf4j.LoggerFactory
import cats.syntax.all._
import cats.effect._

object Uploads {

  val logger = LoggerFactory.getLogger("Uploads")

  def detectExtension(config: AppConfig, item: UrlData.Item): String = {
    ContentTypes.contentTypeExtensions.get(item.contentType).fold(
      throw ErrorCode.InvalidRequest)(
      ext => ext
    )
  }

  def mkAbsoluteFileForUpload(config: AppConfig, item: UrlData.Item, hash: String): File = {
    val ext = detectExtension(config, item)
    val filename = s"$hash.$ext"
    StaticFiles.mkUploadFile(config, filename)
  }

  // stores an item
  def store(config: AppConfig, item: UrlData.Item, hash: String): IO[File] = {

    val uploadFile = mkAbsoluteFileForUpload(config, item, hash)
    logger.info(s"uploading file to ${uploadFile.getPath} with content type ${item.contentType.toString} size ${item.bytes.size}")

    Either.catchNonFatal(Files.write(uploadFile.toPath, item.bytes)).fold(
      err => {
        logger.error(err.getMessage, err)
        IO.raiseError(ErrorCode.InvalidRequest)
      },
      res => IO.pure(uploadFile)
    )

  }

  def deleteIfExists(config: AppConfig, uploadName: String): Unit = {
    val file = StaticFiles.mkUploadFile(config, uploadName)
    if (file.exists) file.delete()
  }

}
