package fink

import java.time.Instant

import cats.effect.IO
import fink.data.JsonInstances._
import fink.data.{AppConfig, UserClaims}
import fink.modules.Authentication
import io.circe.parser
import io.circe.syntax._
import org.http4s._
import org.specs2.matcher.ThrownMessages
import org.specs2.mutable.Specification
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

class AuthTests extends Specification with ThrownMessages {

  "Should encode/decode user claims" in {
    val value = UserClaims(42)
    val text = """{"userId":42}"""

    value.asJson.noSpaces should_== text
    parser.decode[UserClaims](text) should_== Right(value)
  }

  "Should decode JWT" in {

    val claims = UserClaims(42)
    val key = "secretK3y"
    val algo = JwtAlgorithm.HS256
    val now = Instant.now.getEpochSecond

    val claim = JwtClaim(
      issuedAt = Some(now),
      content = claims.asJson.noSpaces
    )

    val token = JwtCirce.encode(claim, key, algo)

    val tokenJson = JwtCirce.decodeJson(token, key, List(algo)).getOrElse(fail("Error decoding token"))

    val claimsDecoded = tokenJson.as[UserClaims].getOrElse(fail("Error reading authUserId from token"))
    claimsDecoded should_== claims

    val issuedAt = tokenJson.hcursor.get[Long]("iat").getOrElse(fail("Error reading iat from token"))
    issuedAt should_== now

  }

  "Should read user claims from Request" in {

    World.config = AppConfig.load()

    val claims = UserClaims(42)
    val key = World.config.authConfig.key
    val algo = JwtAlgorithm.HS256
    val now = Instant.now.getEpochSecond

    val claim = JwtClaim(
      issuedAt = Some(now),
      content = claims.asJson.noSpaces
    )

    val token = JwtCirce.encode(claim, key, algo)

    val req = Request[IO](Method.GET)
      .putHeaders(
        headers.Cookie(RequestCookie("ui", token))
      )

    val res = Authentication.readUserClaims(req)

    res should_== Right(UserClaims(42))

  }

}
