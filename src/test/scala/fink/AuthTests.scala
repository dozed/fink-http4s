package fink

import java.time.Instant

import cats.effect.IO
import fink.data.AppConfig
import fink.modules.AuthModule
import org.http4s._
import org.specs2.matcher.ThrownMessages
import org.specs2.mutable.Specification
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

class AuthTests extends Specification with ThrownMessages {

  "Should decode JWT" in {

    val userId = 42
    val key = "secretK3y"
    val algo = JwtAlgorithm.HS256
    val now = Instant.now.getEpochSecond

    val claim = JwtClaim(
      issuedAt = Some(now),
      content = f"""{"authUserId":$userId}"""
    )

    val token = JwtCirce.encode(claim, key, algo)

    val tokenJson = JwtCirce.decodeJson(token, key, List(algo)).getOrElse(fail("Error decoding token"))

    val authUserId = tokenJson.hcursor.get[Long]("authUserId").getOrElse(fail("Error reading authUserId from token"))
    authUserId should_== userId

    val issuedAt = tokenJson.hcursor.get[Long]("iat").getOrElse(fail("Error reading iat from token"))
    issuedAt should_== now

  }

  "Should read userId from Request" in {

    World.config = AppConfig.load()

    val userId = 42
    val key = World.config.authConfig.key
    val algo = JwtAlgorithm.HS256
    val now = Instant.now.getEpochSecond

    val claim = JwtClaim(
      issuedAt = Some(now),
      content = f"""{"authUserId":$userId}"""
    )

    val token = JwtCirce.encode(claim, key, algo)

    val req = Request[IO](Method.GET)
      .putHeaders(
        headers.Cookie(RequestCookie("ui", token))
      )

    val res = AuthModule.readUserId(req)

    res should_== Right(42)

  }

}
