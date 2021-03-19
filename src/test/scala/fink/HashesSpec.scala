package fink

import fink.util.Hashes
import org.specs2.mutable.Specification

class HashesSpec extends Specification {

  "Hashes should generate a valid MD5 hash" in {

    val txt = "Birch groove"
    val expected = "527eb19b702906be01f964c2e1259e78"

    Hashes.md5(txt) should_== expected

  }

  "Hashes should generate a valid SHA256 hash" in {

    val txt = "Birch groove"
    val expected = "5b0c068bf6fc6b4d24031e41046b91e48e22066e2715240960a7d96804c88b54"

    Hashes.sha256(txt) should_== expected

  }

}
