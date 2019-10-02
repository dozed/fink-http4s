package fink.data

import io.circe._

object JsonInstances {

  implicit def createPostOperationDecoder: Decoder[Operation.CreatePost] =
    Decoder.instance[Operation.CreatePost] { h =>
      for {
        title <- h.downField("title").as[String]
        text <- h.downField("text").as[String]
      } yield {
        Operation.CreatePost(title, text)
      }
    }

  implicit def createdPostNotificationEncoder: Encoder[Notification.CreatedPost] =
    Encoder.instance[Notification.CreatedPost] { msg =>

      Json.obj(
        "type" -> Json.fromString("CreatedPost"),
        "post" -> Json.obj(
          "id" -> Json.fromLong(msg.post.id),
          "title" -> Json.fromString(msg.post.title),
          "text" -> Json.fromString(msg.post.text),
          "date" -> Json.fromLong(msg.post.date),
          "shortlink" -> Json.fromString(msg.post.shortlink),
        ),
        "author" -> Json.obj(
          "id" -> Json.fromLong(msg.author.id),
          "name" -> Json.fromString(msg.author.name)
        ),
      )

    }

}
