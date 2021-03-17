package fink.db

import cats.free.Free
import cats.instances.all._
import cats.syntax.all._
import doobie._
import doobie.implicits._
import fink.data._
import org.http4s.MediaType

object ImageDAO {

  def create(date: Long, title: String, authorId: Long, hash: String, extension: String, contentType: MediaType, fileName: String): ConnectionIO[Image] = {
    sql"INSERT INTO images (date, title, authorid, hash, extension, contenttype, filename) VALUES ($date, $title, $authorId, $hash, $extension, $contentType, $fileName)"
      .update
      .withUniqueGeneratedKeys("id", "date", "title", "authorid", "hash", "extension", "contenttype", "filename")
  }

  def findAll: ConnectionIO[List[Image]] = {
     sql"SELECT i.id, i.date, i.title, i.authorId, i.hash, i.extension, i.contentType, i.filename FROM images i".query[Image].to[List]
  }

  def findById(imageId: Long): ConnectionIO[Option[Image]] = {
    sql"SELECT i.id, i.date, i.title, i.authorId, i.hash, i.extension, i.contentType, i.filename FROM images i WHERE id = $imageId".query[Image].option
  }

  def update(imageId: Long, title: String, hash: String, extension: String, contentType: MediaType, fileName: String): ConnectionIO[Int] = {
    sql"UPDATE images SET title=$title, hash=$hash, extension=$extension, contentType=$contentType, fileName=$fileName WHERE id = $imageId".update.run
  }

  def delete(imageId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM images WHERE id = $imageId".update.run
  }


  def create(title: String, hash: String, extension: String, contentType: MediaType, fileName: String, author: User): ConnectionIO[ImageInfo] = {
    for {
      image <- ImageDAO.create(mkTime, title, author.id, hash, extension, contentType, fileName)
    } yield {
      ImageInfo(image, author)
    }
  }

  def updateImage(imageId: Long, title: String, hash: String, extension: String, contentType: MediaType, fileName: String): ConnectionIO[ImageInfo] = {
    for {
      imageOpt <- ImageDAO.findById(imageId)
      image = imageOpt.get
      authorOpt <- UserDAO.findById(image.authorId)
      author = authorOpt.get
      _ <- ImageDAO.update(imageId, title, hash, extension, contentType, fileName)
    } yield {
      val image1 = image.copy(title = title, hash = hash, extension = extension, contentType = contentType, filename = fileName)
      ImageInfo(image1, author)
    }
  }

  def findImageInfoById(imageId: Long): ConnectionIO[Option[ImageInfo]] = {
    for {
      imageMaybe <- findById(imageId)
      authorMaybe <- {
        imageMaybe.fold(
          Option.empty[User].pure[ConnectionIO]
        )(
          i => UserDAO.findById(i.authorId)
        )
      }
    } yield {
      (imageMaybe, authorMaybe).mapN((image, author) => ImageInfo(image, author))
    }
  }

}
