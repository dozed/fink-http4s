package fink.db

import cats.free.Free
import cats.syntax.all._
import cats.instances.all._
import doobie._
import doobie.implicits._
import fink.data._

object PostDAO {


  def create(date: Long, title: String, authorId: Long, shortlink: String, value: String): ConnectionIO[Post] = {
    sql"INSERT INTO posts (date, title, authorid, shortlink, value) VALUES ($date, $title, $authorId, $shortlink, $value)"
      .update
      .withUniqueGeneratedKeys("id", "date", "title", "authorid", "shortlink", "value")
  }

  def findAll: ConnectionIO[List[Post]] = {
    sql"SELECT id, date, title, authorId, shortlink, value FROM posts".query[Post].to[List]
  }

  def findById(postId: Long): ConnectionIO[Option[Post]] = {
    sql"SELECT id, date, title, authorId, shortlink, value FROM posts WHERE id = $postId".query[Post].option
  }

  def update(postId: Long, title: String, shortlink: String, text: String): ConnectionIO[Int] = {
    sql"UPDATE posts SET title=$title, shortlink=$shortlink, value=$text WHERE id = $postId".update.run
  }

  def delete(postId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM posts WHERE id = $postId".update.run
  }

  def addTag(postId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"INSERT INTO posts_tags (postId, tagId) VALUES ($postId, $tagId)".update.run
  }

  def removeTag(postId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"DELETE FROM posts_tags WHERE postId=$postId AND tagId=$tagId".update.run
  }

  def findTags(postId: Long): ConnectionIO[List[Tag]] = {
    sql"SELECT t.* FROM tags t, posts_tags pt WHERE pt.postId = $postId and pt.tagId = t.id".query[Tag].to[List]
  }


  def addTag(postId: Long, tagValue: String): ConnectionIO[Tag] = {
    for {
      tagMaybe <- TagDAO.findByValue(tagValue)
      tag <- tagMaybe.fold(TagDAO.create(tagValue))(tag => Free.pure(tag))
      _ <- PostDAO.addTag(postId, tag.id)
    } yield {
      tag
    }
  }

  def removeTag(postId: Long, tagValue: String): ConnectionIO[Unit] = {
    for {
      tagMaybe <- TagDAO.findByValue(tagValue)
      _ <- tagMaybe.fold[ConnectionIO[Unit]](Free.pure(()))(tag => PostDAO.removeTag(postId, tag.id).void)
    } yield {
      ()
    }
  }

  def create(title: String, text: String, author: User, tags: List[String]): ConnectionIO[PostInfo] = {
    val shortlink = title.toLowerCase.replaceAllLiterally(" ", "-")

    for {
      post <- PostDAO.create(mkTime, title, author.id, shortlink, text)
      tags <- {
        tags.map(t => PostDAO.addTag(post.id, t)).sequence
      }
    } yield {
      PostInfo(post, tags, author)
    }
  }

  def update(postId: Long, title: String, text: String, shortlink: String, tags: List[String]): ConnectionIO[PostInfo] = {
    for {
      postOpt <- PostDAO.findById(postId)
      post = postOpt.get
      authorOpt <- UserDAO.findById(post.authorId)
      author = authorOpt.get
      _ <- PostDAO.update(postId, title, shortlink, text)
      currentTags <- PostDAO.findTags(postId)
      tagsToDelete = currentTags.filter(t => !tags.contains(t))
      tagsToAdd = tags.filter(t => !currentTags.contains(t))
      _ <- tagsToDelete.map(t => PostDAO.removeTag(post.id, t.id)).sequence
      _ <- tagsToAdd.map(t => PostDAO.addTag(post.id, t)).sequence
      tags <- PostDAO.findTags(postId)
    } yield {
      val post1 = post.copy(title = title, shortlink = shortlink, text = text)
      PostInfo(post1, tags, author)
    }
  }

  def findPostInfoById(postId: Long): ConnectionIO[Option[PostInfo]] = {
    for {
      postMaybe <- findById(postId)
      tags <- PostDAO.findTags(postId)
      authorMaybe <- {
        postMaybe.fold(
          Option.empty[User].pure[ConnectionIO]
        )(
          p => UserDAO.findById(p.authorId)
        )
      }
    } yield {
      (postMaybe, authorMaybe).mapN((post, author) => PostInfo(post, tags, author))
    }
  }

}
