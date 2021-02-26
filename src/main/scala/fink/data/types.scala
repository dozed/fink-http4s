package fink.data

import cats.{Eq, Show}
import cats.syntax.show._
import doobie.{Get, Read}
import doobie.util.Put

case class User(
  id: Long,
  name: String,
  password: String,
  roles: Set[UserRole]
)

object User {

  // val publicUser = User(0, "public", null, UserType.Public)

  implicit val userRead: Read[User] =
    Read[(Long, String, String)].map {
      case (id, name, pass) => User(id, name, pass, Set.empty)
    }

}

trait UserType

object UserType {
  object Registered extends UserType
  object Public extends UserType
}

trait UserRole

object UserRole {
  object CanEdit extends UserRole

  implicit val userRoleEq: Eq[UserRole] = Eq.fromUniversalEquals

  implicit val userRoleShow: Show[UserRole] =
    Show.show {
      case CanEdit => "CanEdit"
    }

  def unsafeFromString(str: String): UserRole = {
    str match {
      case "CanEdit" => CanEdit
    }
  }

  implicit val userRolePut: Put[UserRole] = Put[String].tcontramap(_.show)
  implicit val userRoleGet: Get[UserRole] = Get[String].tmap(UserRole.unsafeFromString)

}

case class UserClaims(
  userId: Long
)

case class Tag(
  id: Long,
  value: String
)

case class Post(
  id: Long,
  date: UnixTime,
  title: String,
  authorId: Long,
  shortlink: String,
  text: String
)

case class Page(
  id: Long,
  date: UnixTime,
  title: String,
  authorId: Long,
  shortlink: String,
  text: String
)

object Page {
  implicit val pageEq: Eq[Page] = Eq.fromUniversalEquals
}

case class Image(
  id: Long,
  date: UnixTime,
  title: String,
  authorId: Long,
  hash: String,
  contentType: String,
  filename: String
)

case class Gallery(
  id: Long,
  coverId: Option[Long],
  date: UnixTime,
  title: String,
  authorId: Long,
  shortlink: String,
  text: String
)

case class Settings(
  title: String,
  description: String,
  keywords: List[String],
  frontend: String,
  uploadDirectory: String
)





// aggregates

case class GalleryInfo(
  gallery: Gallery,
  tags: List[Tag],
  author: User,
  images: List[Image],
  cover: Option[Image],
)

case class ImageInfo(
  image: Image,
  author: User
)

case class PostInfo(
  post: Post,
  tags: List[Tag],
  author: User
)

case class PageInfo(
  page: Page,
  tags: List[Tag],
  author: User
)
