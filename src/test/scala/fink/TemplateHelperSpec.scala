package fink

import fink.data.Post
import fink.media.TemplateHelper
import org.specs2.mutable.Specification

class TemplateHelperSpec extends Specification {

  "TemplateHelper" should {

    "create a Post URI string" in {

      val post = Post(1L, 1616692910000L, "foocaffee", 1L, "foocaffee", "foo caffee")

      val uri = TemplateHelper.postUri(post)

      uri should_== "/2021/03/25/foocaffee/"

    }

    "create a slug" in {

      val slug = TemplateHelper.slug("Foo Caffee!")

      slug should_== "foo-caffee"

    }

  }

}
