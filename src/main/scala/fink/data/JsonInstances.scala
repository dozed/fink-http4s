package fink.data

import io.circe._

object JsonInstances {

  implicit def createPostOperationDecoder: Decoder[Operation.CreatePost] =
    Decoder.instance[Operation.CreatePost] { h =>
      for {
        title <- h.downField("title").as[String]
        text <- h.downField("text").as[String]
        tags <- h.downField("tags").as[List[String]]
      } yield {
        Operation.CreatePost(title, text, tags)
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

}
