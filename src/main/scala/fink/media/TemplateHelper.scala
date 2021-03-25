package fink.media

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

import fink.data.Post

object TemplateHelper {

  val yearFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy").withZone(ZoneId.systemDefault())
  val monthFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MM").withZone(ZoneId.systemDefault())
  val dayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd").withZone(ZoneId.systemDefault())

  def postUri(post: Post): String = {
    val y = yearFormat.format(Instant.ofEpochMilli(post.date))
    val m = monthFormat.format(Instant.ofEpochMilli(post.date))
    val d = dayFormat.format(Instant.ofEpochMilli(post.date))
    s"/$y/$m/$d/${post.shortlink}/"
  }

  def slug(s: String): String = {
    s.toLowerCase.replaceAll("""[^a-z0-9\s-]""", "")
      .replaceAll("""[\s-]+""", " ").trim
      .replaceAll("""\s""", "-")
  }

}