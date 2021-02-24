package fink

import fink.FinkArbitrary._
import fink.data.JsonInstances._
import fink.data._
import io.circe.parser
import io.circe.syntax._
import io.circe.testing.CodecTests
import org.scalacheck.Prop.forAll
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.typelevel.discipline.specs2.mutable.Discipline

 class JsonTests extends Specification with ScalaCheck with Discipline {

 	"Should write/read posts" in {
 		val value = Post(1, 2L, "title", 42, "shortlink", "text")
    val good = """{"id":1,"date":2,"title":"title","authorId":42,"shortlink":"shortlink","text":"text"}"""

 		value.asJson.noSpaces should_== good
    parser.decode[Post](good) should_== Right(value)

    forAll { (p:Post) =>
      p.asJson.as[Post] should_== Right(p)
    }

 	}

 	"Should write/read pages" in {
 		val value = Page(1, 2L, "title", 42, "shortlink", "text")
    val good = """{"id":1,"date":2,"title":"title","authorId":42,"shortlink":"shortlink","text":"text"}"""

 		value.asJson.noSpaces should_== good
    parser.decode[Page](good) should_== Right(value)

    checkAll("Codec[Page]", CodecTests[Page].codec)

 	}

 }
