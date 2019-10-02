package fink.db

import doobie._
import doobie.implicits._

import fink.data._

object DAO {


  def createTag(value: String): ConnectionIO[Tag] = {
    sql"INSERT INTO tags (value) VALUES ($value)"
      .update
      .withUniqueGeneratedKeys("id", "value")
  }

  def createUser(name: String, password: String): ConnectionIO[User] = {
    sql"INSERT INTO users (name, password) VALUES ($name, $password)"
      .update
      .withUniqueGeneratedKeys("id", "name", "password")
  }

  def createPost(date: Long, title: String, authorId: Long, shortlink: String, value: String): ConnectionIO[Post] = {
    sql"INSERT INTO posts (date, title, authorid, shortlink, value) VALUES ($date, $title, $authorId, $shortlink, $value)"
      .update
      .withUniqueGeneratedKeys("id", "date", "title", "authorid", "shortlink", "value")
  }

  def updatePost(postId: Long, title: String, shortlink: String, value: String): ConnectionIO[Unit] = {
    sql"UPDATE posts SET title=$title, shortlink=$shortlink, value=$value WHERE id = $postId".update.run
  }


}
