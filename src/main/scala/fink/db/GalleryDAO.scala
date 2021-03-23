package fink.db

import cats.free.Free
import cats.instances.all._
import cats.syntax.all._
import doobie._
import doobie.implicits._
import fink.data._

object GalleryDAO {


  def create(coverId: Long, date: Long, title: String, authorId: Long, shortlink: String, description: String): ConnectionIO[Gallery] = {
    sql"INSERT INTO galleries (coverId, date, title, authorid, shortlink, description) VALUES (null, $date, $title, $authorId, $shortlink, $description)"
      .update
      .withUniqueGeneratedKeys("id", "coverid", "date", "title", "authorid", "shortlink", "description")
  }

  def findAll: ConnectionIO[List[Gallery]] = {
    sql"SELECT id, coverId, date, title, authorId, shortlink, description FROM galleries".query[Gallery].to[List]
  }

  def findById(galleryId: Long): ConnectionIO[Option[Gallery]] = {
    sql"SELECT id, coverId, date, title, authorId, shortlink, description FROM galleries WHERE id = $galleryId".query[Gallery].option
  }

  def update(galleryId: Long, coverId: Long, title: String, shortlink: String, description: String): ConnectionIO[Int] = {
    sql"UPDATE galleries SET title=$title, shortlink=$shortlink, description=$description WHERE id = $galleryId".update.run
    // sql"UPDATE galleries SET coverId=$coverId, title=$title, shortlink=$shortlink, description=$description WHERE id = $galleryId".update.run
  }

  def delete(galleryId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM galleries WHERE id = $galleryId".update.run
  }

  def findImagesByGalleryId(galleryId: Long): ConnectionIO[List[Image]] = {
    sql"SELECT i.id, i.date, i.title, i.authorId, i.hash, i.extension, i.contentType, i.filename FROM images i, galleries_images gi WHERE gi.imageId = i.id AND gi.galleryId = $galleryId".query[Image].to[List]
  }

  def addImage(galleryId: Long, imageId: Long): ConnectionIO[Int] = {
    sql"INSERT INTO galleries_images (galleryId, imageId) VALUES ($galleryId, $imageId)".update.run
  }

  def removeImage(galleryId: Long, imageId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM galleries_images WHERE galleryId = $galleryId AND imageId = $imageId".update.run
  }

  def addTag(galleryId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"INSERT INTO galleries_tags (galleryId, tagId) VALUES ($galleryId, $tagId)".update.run
  }

  def removeTag(galleryId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM galleries_tags WHERE galleryId=$galleryId AND tagId=$tagId".update.run
  }

  def findTags(galleryId: Long): ConnectionIO[List[Tag]] = {
    sql"SELECT t.* FROM tags t, galleries_tags pt WHERE pt.galleryId = $galleryId and pt.tagId = t.id".query[Tag].to[List]
  }


  def addTag(galleryId: Long, tagValue: String): ConnectionIO[Tag] = {
    for {
      tagMaybe <- TagDAO.findByValue(tagValue)
      tag <- tagMaybe.fold(TagDAO.create(tagValue))(tag => Free.pure(tag))
      _ <- GalleryDAO.addTag(galleryId, tag.id)
    } yield {
      tag
    }
  }

  def removeTag(galleryId: Long, tagValue: String): ConnectionIO[Unit] = {
    for {
      tagMaybe <- TagDAO.findByValue(tagValue)
      _ <- tagMaybe.fold[ConnectionIO[Unit]](Free.pure(()))(tag => GalleryDAO.removeTag(galleryId, tag.id).void)
    } yield {
      ()
    }
  }

  def create(title: String, description: String, author: User, tags: List[String]): ConnectionIO[GalleryInfo] = {
    val shortlink = title.toLowerCase.replace(" ", "-")

    for {
      gallery <- GalleryDAO.create(0, mkTime, title, author.id, shortlink, description)
      tags <- tags.traverse(t => GalleryDAO.addTag(gallery.id, t))
    } yield {
      GalleryInfo(gallery, tags, author, List(), None)
    }
  }

  def update(galleryId: Long, title: String, description: String, shortlink: String, tags: List[String]): ConnectionIO[GalleryInfo] = {
    for {
      galleryOpt <- GalleryDAO.findById(galleryId)
      gallery = galleryOpt.get
      authorOpt <- UserDAO.findById(gallery.authorId)
      author = authorOpt.get
      _ <- GalleryDAO.update(galleryId, 0, title, shortlink, description)
      currentTags <- GalleryDAO.findTags(galleryId)
      tagsToDelete = currentTags.filter(t => !tags.contains(t))
      tagsToAdd = tags.filter(t => !currentTags.contains(t))
      _ <- tagsToDelete.traverse(t => GalleryDAO.removeTag(gallery.id, t.id))
      _ <- tagsToAdd.traverse(t => GalleryDAO.addTag(gallery.id, t))
      tags <- GalleryDAO.findTags(galleryId)
    } yield {
      val gallery1 = gallery.copy(title = title, shortlink = shortlink, text = description)
      GalleryInfo(gallery1, tags, author, List(), None)
    }
  }

  def findGalleryInfoById(galleryId: Long): ConnectionIO[Option[GalleryInfo]] = {
    for {
      galleryMaybe <- findById(galleryId)
      tags <- GalleryDAO.findTags(galleryId)
      authorMaybe <- {
        galleryMaybe.fold(
          Option.empty[User].pure[ConnectionIO]
        )(
          g => UserDAO.findById(g.authorId)
        )
      }
      images <- {
        GalleryDAO.findImagesByGalleryId(galleryId)
      }
    } yield {
      (galleryMaybe, authorMaybe).mapN((page, author) => GalleryInfo(page, tags, author, images, None))
    }
  }

}
