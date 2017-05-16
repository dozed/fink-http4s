package fink.data

case class User(
  id: Long,
  name: String,
  password: String
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
  author: String,
  shortlink: String,
  text: String
)

case class Image(
  id: Long,
  date: UnixTime,
  title: String,
  author: String,
  hash: String,
  contentType: String,
  filename: String
)

case class Gallery(
  id: Long,
  coverId: Long,
  date: UnixTime,
  title: String,
  author: String,
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



// views

case class UserIdName(
  id: Long,
  name: String
)



// aggregates, documents

case class GalleryDocument(
  gallery: Gallery,
  images: List[Image],
  tags: List[Tag],
  cover: Option[Image]
)

case class PageDocument(
  page: Page,
  tags: List[Tag]
)

case class PostDocument(
  post: Post,
  tags: List[Tag]
)
