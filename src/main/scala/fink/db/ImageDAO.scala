package fink.db

import cats.free.Free
import cats.instances.all._
import cats.syntax.all._
import doobie._
import doobie.implicits._
import fink.data._

object ImageDAO {

  def create(date: Long, title: String, authorId: Long, hash: String, contentType: String, fileName: String): ConnectionIO[Image] = {
    sql"INSERT INTO images (date, title, authorid, hash, contenttype, filename) VALUES ($date, $title, $authorId, $hash, $contentType, $fileName)"
      .update
      .withUniqueGeneratedKeys("id", "date", "title", "authorid", "hash", "contenttype", "filename")
  }

  def findAll: ConnectionIO[List[Image]] = {
    sql"SELECT * FROM images".query[Image].to[List]
  }

  def findById(imageId: Long): ConnectionIO[Option[Image]] = {
    sql"SELECT * FROM images WHERE id = $imageId".query[Image].option
  }

  def update(imageId: Long, title: String, hash: String, contentType: String, fileName: String): ConnectionIO[Int] = {
    sql"UPDATE images SET title=$title, hash=$hash, contentType=$contentType, fileName=$fileName WHERE id = $imageId".update.run
  }

  def delete(imageId: Long): ConnectionIO[Int] = {
    sql"DELETE images WHERE id = $imageId".update.run
  }


  def create(title: String, hash: String, contentType: String, fileName: String, author: User): ConnectionIO[ImageInfo] = {
    for {
      image <- ImageDAO.create(mkTime, title, author.id, hash, contentType, fileName)
    } yield {
      ImageInfo(image, author)
    }
  }

  def updateImage(imageId: Long, title: String, hash: String, contentType: String, fileName: String): ConnectionIO[ImageInfo] = {
    for {
      imageOpt <- ImageDAO.findById(imageId)
      image = imageOpt.get
      authorOpt <- UserDAO.findById(image.authorId)
      author = authorOpt.get
      _ <- ImageDAO.update(imageId, title, hash, contentType, fileName)
    } yield {
      val image1 = image.copy(title = title, hash = hash, contentType = contentType, filename = fileName)
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
