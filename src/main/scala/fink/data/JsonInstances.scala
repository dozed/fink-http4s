package fink.data

import cats.syntax.show._
import io.circe._
import io.circe.syntax._
import org.http4s.MediaType

object JsonInstances {

  implicit def createPostOperationDecoder: Decoder[Operation.CreatePost] =
    Decoder.instance[Operation.CreatePost] { h =>
      for {
        title <- h.downField("title").as[String]
        text <- h.downField("text").as[String]
        tags <- h.downField("tags").as[List[String]]
        shortlink <- h.downField("shortlink").as[String]
      } yield {
        Operation.CreatePost(title, text, shortlink, tags)
      }
    }

  implicit def updatePostOperationDecoder: Decoder[Operation.UpdatePost] =
    Decoder.instance[Operation.UpdatePost] { h =>
      for {
        id <- h.downField("id").as[Long]
        title <- h.downField("title").as[String]
        text <- h.downField("text").as[String]
        tags <- h.downField("tags").as[List[String]]
        shortlink <- h.downField("shortlink").as[String]
      } yield {
        Operation.UpdatePost(id, title, text, shortlink, tags)
      }
    }

  implicit def createdPostNotificationEncoder: Encoder[Notification.CreatedPost] =
    Encoder.instance[Notification.CreatedPost] { msg =>

      Json.obj(
        "type" -> Json.fromString("CreatedPost"),
        "post" -> Json.obj(
          "id" -> Json.fromLong(msg.postInfo.post.id),
          "title" -> Json.fromString(msg.postInfo.post.title),
          "text" -> Json.fromString(msg.postInfo.post.text),
          "date" -> Json.fromLong(msg.postInfo.post.date),
          "shortlink" -> Json.fromString(msg.postInfo.post.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.postInfo.author.id),
          "name" -> Json.fromString(msg.postInfo.author.name)
        ),
        "tags" -> Json.arr(msg.postInfo.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def updatedPostNotificationEncoder: Encoder[Notification.UpdatedPost] =
    Encoder.instance[Notification.UpdatedPost] { msg =>

      Json.obj(
        "type" -> Json.fromString("UpdatedPost"),
        "post" -> Json.obj(
          "id" -> Json.fromLong(msg.postInfo.post.id),
          "title" -> Json.fromString(msg.postInfo.post.title),
          "text" -> Json.fromString(msg.postInfo.post.text),
          "date" -> Json.fromLong(msg.postInfo.post.date),
          "shortlink" -> Json.fromString(msg.postInfo.post.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.postInfo.author.id),
          "name" -> Json.fromString(msg.postInfo.author.name)
        ),
        "tags" -> Json.arr(msg.postInfo.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def postInfoEncoder: Encoder[PostInfo] =
    Encoder.instance[PostInfo] { info =>

      Json.obj(
        "post" -> Json.obj(
          "id" -> Json.fromLong(info.post.id),
          "title" -> Json.fromString(info.post.title),
          "text" -> Json.fromString(info.post.text),
          "date" -> Json.fromLong(info.post.date),
          "shortlink" -> Json.fromString(info.post.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(info.author.id),
          "name" -> Json.fromString(info.author.name)
        ),
        "tags" -> Json.arr(info.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def postEncoder: Encoder[Post] =
    Encoder.instance[Post] { post =>

      Json.obj(
        "id" -> Json.fromLong(post.id),
        "date" -> Json.fromLong(post.date),
        "title" -> Json.fromString(post.title),
        "authorId" -> Json.fromLong(post.authorId),
        "shortlink" -> Json.fromString(post.shortlink),
        "text" -> Json.fromString(post.text),
      )

    }

  implicit def postDecoder: Decoder[Post] =
    Decoder.forProduct6[Post, Long, Long, String, Long, String, String](
      "id", "date", "title", "authorId", "shortlink", "text"
    )(Post.apply)

  implicit def pageDecoder: Decoder[Page] =
    Decoder.forProduct6[Page, Long, Long, String, Long, String, String](
      "id", "date", "title", "authorId", "shortlink", "text"
    )(Page.apply)


  implicit def createPageOperationDecoder: Decoder[Operation.CreatePage] =
    Decoder.forProduct4[Operation.CreatePage, String, String, List[String], String]("title", "text", "tags", "shortlink")(
      (title, text, tags, shortlink) => Operation.CreatePage(title, text, shortlink, tags)
    )

  implicit def updatePageOperationDecoder: Decoder[Operation.UpdatePage] =
    Decoder.forProduct5[Operation.UpdatePage, Long, String, String, List[String], String]("id", "title", "text", "tags", "shortlink")(
      (id, title, text, tags, shortlink) => Operation.UpdatePage(id, title, text, shortlink, tags)
    )

  implicit def createdPageNotificationEncoder: Encoder[Notification.CreatedPage] =
    Encoder.instance[Notification.CreatedPage] { msg =>

      Json.obj(
        "type" -> Json.fromString("CreatedPage"),
        "page" -> Json.obj(
          "id" -> Json.fromLong(msg.pageInfo.page.id),
          "title" -> Json.fromString(msg.pageInfo.page.title),
          "text" -> Json.fromString(msg.pageInfo.page.text),
          "date" -> Json.fromLong(msg.pageInfo.page.date),
          "shortlink" -> Json.fromString(msg.pageInfo.page.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.pageInfo.author.id),
          "name" -> Json.fromString(msg.pageInfo.author.name)
        ),
        "tags" -> Json.arr(msg.pageInfo.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def updatedPageNotificationEncoder: Encoder[Notification.UpdatedPage] =
    Encoder.instance[Notification.UpdatedPage] { msg =>

      Json.obj(
        "type" -> Json.fromString("UpdatedPage"),
        "page" -> Json.obj(
          "id" -> Json.fromLong(msg.pageInfo.page.id),
          "title" -> Json.fromString(msg.pageInfo.page.title),
          "text" -> Json.fromString(msg.pageInfo.page.text),
          "date" -> Json.fromLong(msg.pageInfo.page.date),
          "shortlink" -> Json.fromString(msg.pageInfo.page.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.pageInfo.author.id),
          "name" -> Json.fromString(msg.pageInfo.author.name)
        ),
        "tags" -> Json.arr(msg.pageInfo.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def pageInfoEncoder: Encoder[PageInfo] =
    Encoder.instance[PageInfo] { info =>

      Json.obj(
        "page" -> Json.obj(
          "id" -> Json.fromLong(info.page.id),
          "title" -> Json.fromString(info.page.title),
          "text" -> Json.fromString(info.page.text),
          "date" -> Json.fromLong(info.page.date),
          "shortlink" -> Json.fromString(info.page.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(info.author.id),
          "name" -> Json.fromString(info.author.name)
        ),
        "tags" -> Json.arr(info.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def pageEncoder: Encoder[Page] =
    Encoder.instance[Page] { page =>

      Json.obj(
        "id" -> Json.fromLong(page.id),
        "date" -> Json.fromLong(page.date),
        "title" -> Json.fromString(page.title),
        "authorId" -> Json.fromLong(page.authorId),
        "shortlink" -> Json.fromString(page.shortlink),
        "text" -> Json.fromString(page.text),
      )

    }


//  implicit def createPageOperationEncoder: Encoder[Tag] =
//    Encoder.forProduct2[Tag, Long, String]("id", "value")((id, value) => Tag(id, value))

  implicit def tagEncoder: Encoder[Tag] =
    Encoder.forProduct2[Tag, Long, String]("id", "value")(tag => (tag.id, tag.value))


  implicit def createGalleryOperationDecoder: Decoder[Operation.CreateGallery] =
    Decoder.forProduct4[Operation.CreateGallery, String, String, List[String], String]("title", "text", "tags", "shortlink")(
      (title, text, tags, shortlink) => Operation.CreateGallery(title, text, shortlink, tags)
    )

  implicit def updateGalleryOperationDecoder: Decoder[Operation.UpdateGallery] =
    Decoder.forProduct5[Operation.UpdateGallery, Long, String, String, List[String], String]("id", "title", "text", "tags", "shortlink")(
      (id, title, text, tags, shortlink) => Operation.UpdateGallery(id, title, text, shortlink, tags)
    )

  implicit def createdGalleryNotificationEncoder: Encoder[Notification.CreatedGallery] =
    Encoder.instance[Notification.CreatedGallery] { msg =>

      Json.obj(
        "type" -> Json.fromString("CreatedGallery"),
        "gallery" -> Json.obj(
          "id" -> Json.fromLong(msg.galleryInfo.gallery.id),
          "title" -> Json.fromString(msg.galleryInfo.gallery.title),
          "text" -> Json.fromString(msg.galleryInfo.gallery.text),
          "date" -> Json.fromLong(msg.galleryInfo.gallery.date),
          "shortlink" -> Json.fromString(msg.galleryInfo.gallery.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.galleryInfo.author.id),
          "name" -> Json.fromString(msg.galleryInfo.author.name)
        ),
        "tags" -> Json.arr(msg.galleryInfo.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def updatedGalleryNotificationEncoder: Encoder[Notification.UpdatedGallery] =
    Encoder.instance[Notification.UpdatedGallery] { msg =>

      Json.obj(
        "type" -> Json.fromString("UpdatedGallery"),
        "gallery" -> Json.obj(
          "id" -> Json.fromLong(msg.galleryInfo.gallery.id),
          "title" -> Json.fromString(msg.galleryInfo.gallery.title),
          "text" -> Json.fromString(msg.galleryInfo.gallery.text),
          "date" -> Json.fromLong(msg.galleryInfo.gallery.date),
          "shortlink" -> Json.fromString(msg.galleryInfo.gallery.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.galleryInfo.author.id),
          "name" -> Json.fromString(msg.galleryInfo.author.name)
        ),
        "tags" -> Json.arr(msg.galleryInfo.tags.map(t => Json.fromString(t.value)):_*),
      )

    }

  implicit def galleryInfoEncoder: Encoder[GalleryInfo] =
    Encoder.instance[GalleryInfo] { info =>

      Json.obj(
        "gallery" -> Json.obj(
          "id" -> Json.fromLong(info.gallery.id),
          "title" -> Json.fromString(info.gallery.title),
          "text" -> Json.fromString(info.gallery.text),
          "date" -> Json.fromLong(info.gallery.date),
          "shortlink" -> Json.fromString(info.gallery.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(info.author.id),
          "name" -> Json.fromString(info.author.name)
        ),
        "tags" -> Json.arr(info.tags.map(t => Json.fromString(t.value)):_*),
        "images" -> Json.arr(info.images.map(t => t.asJson):_*),
      )

    }

  implicit def galleryEncoder: Encoder[Gallery] =
    Encoder.instance[Gallery] { gallery =>

      Json.obj(
        "id" -> Json.fromLong(gallery.id),
        "title" -> Json.fromString(gallery.title),
        "text" -> Json.fromString(gallery.text),
        "date" -> Json.fromLong(gallery.date),
        "shortlink" -> Json.fromString(gallery.shortlink),
      )

    }

  implicit def uploadImageToGalleryOperationDecoder: Decoder[Operation.UploadImageToGallery] =
    Decoder.forProduct3[Operation.UploadImageToGallery, Long, String, String]("galleryId", "title", "imageData")(
      (galleryId, title, imageData) => Operation.UploadImageToGallery(galleryId, title, imageData)
    )

  implicit def removeImageFromGalleryOperationDecoder: Decoder[Operation.RemoveImageFromGallery] =
    Decoder.forProduct2[Operation.RemoveImageFromGallery, Long, Long]("galleryId", "imageId")(
      (galleryId, imageId) => Operation.RemoveImageFromGallery(galleryId, imageId)
    )

  implicit def createImageOperationDecoder: Decoder[Operation.CreateImage] =
    Decoder.forProduct2[Operation.CreateImage, String, String]("title", "imageData")(
      (title, imageData) => Operation.CreateImage(title, imageData)
    )

  implicit def updateImageOperationDecoder: Decoder[Operation.UpdateImage] =
    Decoder.forProduct2[Operation.UpdateImage, Long, String]("id", "title")(
      (id, title) => Operation.UpdateImage(id, title)
    )

  implicit def createdImageNotificationEncoder: Encoder[Notification.CreatedImage] =
    Encoder.instance[Notification.CreatedImage] { msg =>

      Json.obj(
        "type" -> Json.fromString("CreatedImage"),
        "image" -> msg.imageInfo.image.asJson,
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.imageInfo.author.id),
          "name" -> Json.fromString(msg.imageInfo.author.name)
        ),
      )

    }

  implicit def updatedImageNotificationEncoder: Encoder[Notification.UpdatedImage] =
    Encoder.instance[Notification.UpdatedImage] { msg =>

      Json.obj(
        "type" -> Json.fromString("UpdatedImage"),
        "image" -> msg.imageInfo.image.asJson,
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.imageInfo.author.id),
          "name" -> Json.fromString(msg.imageInfo.author.name)
        ),
      )

    }

  implicit def imageInfoEncoder: Encoder[ImageInfo] =
    Encoder.instance[ImageInfo] { info =>

      Json.obj(
        "image" -> info.image.asJson,
        "author" -> Json.obj(
          "id" -> Json.fromLong(info.author.id),
          "name" -> Json.fromString(info.author.name)
        ),
      )

    }

  implicit def imageEncoder: Encoder[Image] =
    Encoder.instance[Image] { image =>

      Json.obj(
        "id" -> Json.fromLong(image.id),
        "title" -> Json.fromString(image.title),
        "hash" -> Json.fromString(image.hash),
        "extension" -> Json.fromString(image.extension),
        "contentType" -> image.contentType.asJson,
        "fileName" -> Json.fromString(image.filename),
        "date" -> Json.fromLong(image.date),
      )

    }


  implicit val sortImageDecoder: Decoder[Operation.SortImage] =
    Decoder.forProduct3[Operation.SortImage, Long, Int, Int]("galleryId", "from", "to")(Operation.SortImage)

  implicit val loginDecoder: Decoder[Operation.Login] =
    Decoder.forProduct2[Operation.Login, String, String]("username", "password")(Operation.Login)

  implicit val userClaimsEncoder: Encoder[UserClaims] =
    Encoder.forProduct1[UserClaims, Long]("userId")(_.userId)

  implicit val userClaimsDecoder: Decoder[UserClaims] =
    Decoder.forProduct1[UserClaims, Long]("userId")(UserClaims)

  implicit def mediaTypeEncoder: Encoder[MediaType] = Encoder[String].contramap(_.show)

}
