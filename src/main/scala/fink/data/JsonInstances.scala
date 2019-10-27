package fink.data

import io.circe._

import scala.reflect.ClassTag

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

  implicit def deletePostOperationDecoder: Decoder[Operation.DeletePost] =
    Decoder.forProduct1[Operation.DeletePost, Long]("id")(id => Operation.DeletePost(id))

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
        "title" -> Json.fromString(post.title),
        "text" -> Json.fromString(post.text),
        "date" -> Json.fromLong(post.date),
        "shortlink" -> Json.fromString(post.shortlink),
      )

    }


  implicit def createPageOperationDecoder: Decoder[Operation.CreatePage] =
    Decoder.forProduct4[Operation.CreatePage, String, String, List[String], String]("title", "text", "tags", "shortlink")(
      (title, text, tags, shortlink) => Operation.CreatePage(title, text, shortlink, tags)
    )

  implicit def updatePageOperationDecoder: Decoder[Operation.UpdatePage] =
    Decoder.forProduct5[Operation.UpdatePage, Long, String, String, List[String], String]("id", "title", "text", "tags", "shortlink")(
      (id, title, text, tags, shortlink) => Operation.UpdatePage(id, title, text, shortlink, tags)
    )

  implicit def deletePageOperationDecoder: Decoder[Operation.DeletePage] =
    Decoder.forProduct1[Operation.DeletePage, Long]("id")(id => Operation.DeletePage(id))

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
        "title" -> Json.fromString(page.title),
        "text" -> Json.fromString(page.text),
        "date" -> Json.fromLong(page.date),
        "shortlink" -> Json.fromString(page.shortlink),
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

  implicit def deleteGalleryOperationDecoder: Decoder[Operation.DeleteGallery] =
    Decoder.forProduct1[Operation.DeleteGallery, Long]("id")(id => Operation.DeleteGallery(id))

  implicit def createdGalleryNotificationEncoder: Encoder[Notification.CreatedGallery] =
    Encoder.instance[Notification.CreatedGallery] { msg =>

      Json.obj(
        "type" -> Json.fromString("CreatedGallery"),
        "page" -> Json.obj(
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
        "page" -> Json.obj(
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
        "page" -> Json.obj(
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



}
