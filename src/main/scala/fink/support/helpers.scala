package fink.support

import fink.data._

import org.joda.time.format._

object TemplateHelper {

  val yearFormat = DateTimeFormat.forPattern("yyyy")
  val monthFormat = DateTimeFormat.forPattern("MM")
  val dayFormat = DateTimeFormat.forPattern("dd")

  def postUri(post: Post) = {
    val y = yearFormat.print(post.date)
    val m = monthFormat.print(post.date)
    val d = dayFormat.print(post.date)
    s"/$y/$m/$d/${post.shortlink}/"
  }

  def slug(s: String) = {
    s.toLowerCase.replaceAll("""[^a-z0-9\s-]""", "")
      .replaceAll("""[\s-]+""", " ").trim
      .replaceAll("""\s""", "-")
  }

}