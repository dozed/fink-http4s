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
    sql"SELECT * FROM posts".query[Post].to[List]
  }

  def findById(postId: Long): ConnectionIO[Option[Post]] = {
    sql"SELECT * FROM posts WHERE id = $postId".query[Post].option
  }

  def update(postId: Long, title: String, shortlink: String, value: String): ConnectionIO[Int] = {
    sql"UPDATE posts SET title=$title, shortlink=$shortlink, value=$value WHERE id = $postId".update.run
  }

  def addTag(postId: Long, tagId: Long): ConnectionIO[Int] = {
    sql"INSERT INTO posts_tags (postId, tagId) VALUES ($postId, $tagId)".update.run
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
