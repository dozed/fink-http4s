package fink.data

case class User(
  id: Long,
  name: String,
  password: String
)

//object User {
//
//  val publicUser = User(0, "public", null, UserType.Public)
//
//}

trait UserType

object UserType {
  object Registered extends UserType
  object Public extends UserType
}

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
  coverId: Long,
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
