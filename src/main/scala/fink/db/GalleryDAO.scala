package fink.db

import cats.free.Free
import cats.instances.all._
import cats.syntax.all._
import doobie._
import doobie.implicits._
import fink.data._

object GalleryDAO {


  def create(coverId: Long, date: Long, title: String, authorId: Long, shortlink: String, description: String): ConnectionIO[Gallery] = {
    sql"INSERT INTO galleries (coverId, date, title, authorid, shortlink, description) VALUES ($coverId, $date, $title, $authorId, $shortlink, $description)"
      .update
      .withUniqueGeneratedKeys("id", "coverId", "date", "title", "authorid", "shortlink", "description")
  }

  def findAll: ConnectionIO[List[Gallery]] = {
    sql"SELECT * FROM galleries".query[Gallery].to[List]
  }

  def findById(galleryId: Long): ConnectionIO[Option[Gallery]] = {
    sql"SELECT * FROM galleries WHERE id = $galleryId".query[Gallery].option
  }

  def update(galleryId: Long, coverId: Long, title: String, shortlink: String, description: String): ConnectionIO[Int] = {
    sql"UPDATE pages SET coverId=$coverId, title=$title, shortlink=$shortlink, description=$description WHERE id = $galleryId".update.run
  }

  def delete(galleryId: Long): ConnectionIO[Int] = {
    sql"DELETE galleries WHERE id = $galleryId".update.run
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
    val shortlink = title.toLowerCase.replaceAllLiterally(" ", "-")

    for {
      gallery <- GalleryDAO.create(0, mkTime, title, author.id, shortlink, description)
      tags <- {
        tags.map(t => GalleryDAO.addTag(gallery.id, t)).sequence
      }
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
      _ <- tagsToDelete.map(t => GalleryDAO.removeTag(gallery.id, t.id)).sequence
      _ <- tagsToAdd.map(t => GalleryDAO.addTag(gallery.id, t)).sequence
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
    } yield {
      (galleryMaybe, authorMaybe).mapN((page, author) => GalleryInfo(page, tags, author, List(), None))
    }
  }

}
