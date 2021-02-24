package fink

import fink.data.JsonInstances._
import fink.data._
import io.circe.parser
import io.circe.syntax._
import org.specs2.mutable._

 class JsonTests extends Specification {

 	"Should write/read posts" in {
 		val value = Post(1, 2L, "title", 42, "shortlink", "text")
    val good = """{"id":1,"date":2,"title":"title","authorId":42,"shortlink":"shortlink","text":"text"}"""

 		value.asJson.noSpaces should_== good
    parser.decode[Post](good) should_== Right(value)
 	}

 }
