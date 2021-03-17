package fink

import cats.syntax.show._
import doobie.Meta
import org.http4s.MediaType

package object db {

  implicit val mediaTypeMeta: Meta[MediaType] = Meta[String].timap(s => MediaType.unsafeParse(s))(_.show)

}
