package fink.media

import org.apache.tika.config.TikaConfig
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.http4s.MediaType

object ContentTypes {

  type Extension = String

  val contentTypeExtensions: Map[MediaType, Extension] = Map(
    MediaType.image.jpeg -> "jpg",
    MediaType.image.png -> "png",
    MediaType.image.gif -> "gif",
  )

  def detectContentType(data: Array[Byte]): MediaType = {
    val config = TikaConfig.getDefaultConfig
    val detector = config.getDetector

    val stream = TikaInputStream.get(data)
    val metadata = new Metadata()
    val mediaType = detector.detect(stream, metadata)

    mediaTypeFromString(s"${mediaType.getType}/${mediaType.getSubtype}")
  }

  def mediaTypeFromString(str: String): MediaType = {
    str match {
      case "image/jpeg" => MediaType.image.jpeg
      case "image/png" => MediaType.image.png
      case "image/gif" => MediaType.image.gif
      case x =>
        MediaType.parse(x).getOrElse(MediaType.application.`octet-stream`)
    }
  }



}
