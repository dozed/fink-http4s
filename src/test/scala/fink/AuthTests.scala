package fink

import java.time.Instant

import org.specs2.mutable.Specification
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

class AuthTests extends Specification {

  "Should decode JWT" in {

    val claim = JwtClaim(
      issuedAt = Some(Instant.now.getEpochSecond),
      content = """{"authUserId":42}"""
    )

    val key = "secretK3y"
    val algo = JwtAlgorithm.HS256
    val token = JwtCirce.encode(claim, key, algo)

    println(token)

    val tokenJson = JwtCirce.decodeJson(token, key, Seq(JwtAlgorithm.HS256)).toEither.getOrElse(sys.error(""))

    val authUserId = tokenJson.hcursor.downField("authUserId").as[Long].getOrElse(sys.error(""))
    authUserId should_== 42

    // Jwt.validate()

  }

}
