package fink.db

import doobie.imports._

object DAO {


  def createTag(id: Long, value: String): ConnectionIO[Int] = {
    sql"INSERT INTO tags (id, value) VALUES ($id, $value)".update.run
  }

  def createUser(id: Long, name: String, password: String): ConnectionIO[Int] = {
    sql"INSERT INTO users (id, name, password) VALUES ($id, $name, $password)".update.run
  }

  def createPost(id: Long, date: Long, title: String, authorId: Long, shortlink: String, value: String): ConnectionIO[Int] = {
    sql"INSERT INTO posts (id, date, title, authorId, shortlink, value) VALUES ($id, $date, $title, $authorId, $shortlink, $value)".update.run
  }


}
