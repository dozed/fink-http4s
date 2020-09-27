package fink.db

import doobie._
import doobie.implicits._
import fink.data._

object TagDAO {

  def create(value: String): ConnectionIO[Tag] = {
    sql"INSERT INTO tags (value) VALUES ($value)"
      .update
      .withUniqueGeneratedKeys("id", "value")
  }

  def findAll: ConnectionIO[List[Tag]] = {
    sql"SELECT * FROM tags".query[Tag].to[List]
  }

  def findById(tagId: Long): ConnectionIO[Option[Tag]] = {
    sql"SELECT * FROM tags WHERE id = $tagId".query[Tag].option
  }

  def findByValue(value: String): ConnectionIO[Option[Tag]] = {
    sql"SELECT * FROM tags WHERE value = $value".query[Tag].option
  }


}
