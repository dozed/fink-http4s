package fink.db

import cats.free.Free
import cats.instances.all._
import cats.syntax.all._
import doobie._
import doobie.implicits._
import fink.data._

object PageDAO {


  def create(date: Long, title: String, authorId: Long, shortlink: String, value: String): ConnectionIO[Page] = {
    sql"INSERT INTO pages (date, title, authorid, shortlink, value) VALUES ($date, $title, $authorId, $shortlink, $value)"
      .update
      .withUniqueGeneratedKeys("id", "date", "title", "authorid", "shortlink", "value")
  }

  def findAll: ConnectionIO[List[Page]] = {
    sql"SELECT * FROM pages".query[Page].to[List]
  }

  def findById(pageId: Long): ConnectionIO[Option[Page]] = {
    sql"SELECT * FROM pages WHERE id = $pageId".query[Page].option
  }

  def update(pageId: Long, title: String, shortlink: String, text: String): ConnectionIO[Int] = {
    sql"UPDATE pages SET title=$title, shortlink=$shortlink, value=$text WHERE id = $pageId".update.run
  }

  def delete(pageId: Long): ConnectionIO[Int] = {
    sql"DELETE pages WHERE id = $pageId".update.run
  }

  def addTag(pageId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"INSERT INTO pages_tags (pageId, tagId) VALUES ($pageId, $tagId)".update.run
  }

  def removeTag(pageId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM pages_tags WHERE pageId=$pageId AND tagId=$tagId".update.run
  }

  def findTags(pageId: Long): ConnectionIO[List[Tag]] = {
    sql"SELECT t.* FROM tags t, pages_tags pt WHERE pt.pageId = $pageId and pt.tagId = t.id".query[Tag].to[List]
  }


  def addTag(pageId: Long, tagValue: String): ConnectionIO[Tag] = {
    for {
      tagMaybe <- TagDAO.findByValue(tagValue)
      tag <- tagMaybe.fold(TagDAO.create(tagValue))(tag => Free.pure(tag))
      _ <- PageDAO.addTag(pageId, tag.id)
    } yield {
      tag
    }
  }

  def removeTag(pageId: Long, tagValue: String): ConnectionIO[Unit] = {
    for {
      tagMaybe <- TagDAO.findByValue(tagValue)
      _ <- tagMaybe.fold[ConnectionIO[Unit]](Free.pure(()))(tag => PageDAO.removeTag(pageId, tag.id).void)
    } yield {
      ()
    }
  }

  def create(title: String, text: String, author: User, tags: List[String]): ConnectionIO[PageInfo] = {
    val shortlink = title.toLowerCase.replaceAllLiterally(" ", "-")

    for {
      page <- PageDAO.create(mkTime, title, author.id, shortlink, text)
      tags <- {
        tags.map(t => PageDAO.addTag(page.id, t)).sequence
      }
    } yield {
      PageInfo(page, tags, author)
    }
  }

  def update(pageId: Long, title: String, text: String, shortlink: String, tags: List[String]): ConnectionIO[PageInfo] = {
    for {
      pageOpt <- PageDAO.findById(pageId)
      page = pageOpt.get
      authorOpt <- UserDAO.findById(page.authorId)
      author = authorOpt.get
      _ <- PageDAO.update(pageId, title, shortlink, text)
      currentTags <- PageDAO.findTags(pageId)
      tagsToDelete = currentTags.filter(t => !tags.contains(t))
      tagsToAdd = tags.filter(t => !currentTags.contains(t))
      _ <- tagsToDelete.map(t => PageDAO.removeTag(page.id, t.id)).sequence
      _ <- tagsToAdd.map(t => PageDAO.addTag(page.id, t)).sequence
      tags <- PageDAO.findTags(pageId)
    } yield {
      val page1 = page.copy(title = title, shortlink = shortlink, text = text)
      PageInfo(page1, tags, author)
    }
  }

  def findPageInfoById(pageId: Long): ConnectionIO[Option[PageInfo]] = {
    for {
      pageMaybe <- findById(pageId)
      tags <- PageDAO.findTags(pageId)
      authorMaybe <- {
        pageMaybe.fold(
          Option.empty[User].pure[ConnectionIO]
        )(
          p => UserDAO.findById(p.authorId)
        )
      }
    } yield {
      (pageMaybe, authorMaybe).mapN((page, author) => PageInfo(page, tags, author))
    }
  }

}
