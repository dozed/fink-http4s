package fink.db

import doobie._
import doobie.implicits._
import fink.data._

object UserDAO {


  def create(name: String, password: String): ConnectionIO[User] = {
    val pass = mkPassword(password)
    sql"INSERT INTO users (name, password) VALUES ($name, $pass)"
      .update
      .withUniqueGeneratedKeys("id", "name", "password")
  }

  def findAll: ConnectionIO[List[User]] = {
    sql"SELECT * FROM users".query[User].to[List]
  }

  def findById(userId: Long): ConnectionIO[Option[User]] = {
    sql"SELECT * FROM users WHERE id = $userId".query[User].option
  }

  def findByName(name: String): ConnectionIO[Option[User]] = {
    sql"SELECT * FROM users WHERE name = $name".query[User].option
  }

  def updateName(userId: Long, name: String): ConnectionIO[Int] = {
    sql"UPDATE users SET name=$name WHERE id = $userId".update.run
  }

  def updatePassword(userId: Long, password: String): ConnectionIO[Int] = {
    sql"UPDATE users SET password=$password WHERE id = $userId".update.run
  }



}
