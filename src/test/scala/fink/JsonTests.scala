package fink

import fink.data._
import fink.data.JsonInstances._

import io.circe.syntax._
import org.specs2.mutable._

 class JsonTests extends Specification {

 	"Should write/read posts" in {
 		val post = Post(1, 2L, "title", 42, "shortlink", "text")

 		val json = post.asJson
 		val post2 = json.as[Post]

 		post2 should_== Right(post)
 	}

 }
