package fink.db

import cats.instances.all._
import cats.syntax.all._
import doobie._
import doobie.implicits._
import fink.data._

object UserDAO {


  def create(name: String, password: String, roles: Set[UserRole]): ConnectionIO[User] = {
    val pass = mkPassword(password)

    for {
      id <- sql"INSERT INTO users (name, password) VALUES ($name, $pass)"
        .update
        .withUniqueGeneratedKeys[Long]("id")
      _ <- roles.toList.traverse(r => addRoleToUser(id, r))
    } yield {
      User(id, name, pass, roles)
    }
  }

  def addRoleToUser(userId: Long, role: UserRole): ConnectionIO[Int] = {
    sql"INSERT INTO users_roles (userId, roleName) VALUES ($userId, $role)".update.run
  }

  def findUserRoles(userId: Long): ConnectionIO[List[UserRole]] = {
    sql"SELECT roleName FROM users_roles WHERE userId=$userId".query[UserRole].to[List]
  }

  def findAll: ConnectionIO[List[User]] = {
    for {
      users <- sql"SELECT id, name, password FROM users".query[User].to[List]
      users <- {
        users.traverse(u => {
          findUserRoles(u.id).map(roles => u.copy(roles = roles.toSet))
        })
      }
    } yield {
      users
    }
  }

  def findById(userId: Long): ConnectionIO[Option[User]] = {
    for {
      userOpt <- sql"SELECT id, name, password FROM users WHERE id = $userId".query[User].option
      userOpt <- {
        userOpt.traverse { user =>
          findUserRoles(user.id).map(roles => user.copy(roles = roles.toSet))
        }
      }
    } yield {
      userOpt
    }
  }

  def findByName(name: String): ConnectionIO[Option[User]] = {
    for {
      userOpt <- sql"SELECT id, name, password FROM users WHERE name = $name".query[User].option
      userOpt <- {
        userOpt.traverse { user =>
          findUserRoles(user.id).map(roles => user.copy(roles = roles.toSet))
        }
      }
    } yield {
      userOpt
    }
  }

  def updateName(userId: Long, name: String): ConnectionIO[Int] = {
    sql"UPDATE users SET name=$name WHERE id = $userId".update.run
  }

  def updatePassword(userId: Long, password: String): ConnectionIO[Int] = {
    sql"UPDATE users SET password=$password WHERE id = $userId".update.run
  }



}
