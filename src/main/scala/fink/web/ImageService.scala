package fink.web

import java.io.{File, FileInputStream, FileOutputStream}

import cats.effect.{Blocker, IO}
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.{GifWriter, ImageWriter, JpegWriter, PngWriter}
import fink.World
import fink.World.cs
import fink.media.StaticFiles
import org.apache.commons.io.IOUtils
import org.http4s.{HttpRoutes, Response, StaticFile}
import org.http4s.dsl.io._

import scala.util.matching.Regex

object ImageService {

  sealed trait ImageSpec
  object ImageSpec {
    case object Full extends ImageSpec
    case class KeepRatio(size: Int) extends ImageSpec
    case class Square(size: Int) extends ImageSpec
  }

  case class ImageName(hash: String, ext: String)


  val FullExpr: Regex = """([a-z0-9]+)-full\.([a-z]+)""".r
  val KeepRatioExpr: Regex = """([a-z0-9]+)-keep-ratio-([0-9]+)\.([a-z]+)""".r
  val SquareExpr: Regex = """([a-z0-9]+)-square-([0-9]+)\.([a-z]+)""".r

  def parseImageSpecAndName(imageStr: String): Option[(ImageSpec, ImageName)] = {
    imageStr match {
      case FullExpr(hash, ext) => Some((ImageSpec.Full, ImageName(hash, ext)))
      case KeepRatioExpr(hash, size, ext) => Some((ImageSpec.KeepRatio(size.toInt), ImageName(hash, ext)))
      case SquareExpr(hash, size, ext) => Some((ImageSpec.Square(size.toInt), ImageName(hash, ext)))
      case _ => None
    }
  }

  def getWriterFromExt(ext: String): ImageWriter = {
    ext match {
      case "jpg" => JpegWriter.Default
      case "jpeg" => JpegWriter.Default
      case "png" => PngWriter.MinCompression
      case "gif" => GifWriter.Default
    }
  }

  def processImage(spec: ImageSpec, imageName: ImageName, uploadedFile: File, targetFile: File): IO[Unit] = IO.delay {
    spec match {
      case ImageSpec.Full =>
        IOUtils.copy(new FileInputStream(uploadedFile), new FileOutputStream(targetFile))

      case ImageSpec.KeepRatio(size) =>
        val image = ImmutableImage.loader().fromFile(uploadedFile)

        val scaledImage =
          if (image.width > image.height) {
            image.scaleToWidth(size)
          } else {
            image.scaleToHeight(size)
          }

        val writer = getWriterFromExt(imageName.ext)

        scaledImage.output(writer, targetFile)

      case ImageSpec.Square(size) =>
        val image = ImmutableImage.loader().fromFile(uploadedFile)
        val scaledImage = image.scaleTo(size, size)
        val writer = getWriterFromExt(imageName.ext)

        scaledImage.output(writer, targetFile)

    }
  }


  def routes(blocker: Blocker): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ GET -> Root / "images" / imageStr =>

      parseImageSpecAndName(imageStr) match {
        case Some((spec, name)) =>
          val uploadedImage = StaticFiles.mkUploadFile(World.config, s"${name.hash}.${name.ext}")
          val publicImage = StaticFiles.mkPublicImageFile(World.config, imageStr)

          if (!uploadedImage.exists) {
            NotFound()
          } else {
            for {
              _ <- processImage(spec, name, uploadedImage, publicImage)
              resOpt <- StaticFile.fromFile(publicImage, blocker, Some(req)).value
              res <- resOpt.map(res => IO.delay(res)).getOrElse(InternalServerError())
            } yield {
              res
            }
          }
        case None => BadRequest()
      }


  }

}
