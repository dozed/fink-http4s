package fink

import java.time.Instant

import cats.effect.IO
import doobie.Transactor
import doobie.implicits._
import fink.World.{config, cs, xa}
import fink.auth.Authentication
import fink.data.JsonInstances._
import fink.data.{AppConfig, ErrorCode, UserClaims}
import fink.db.{DbSetup, UserDAO}
import io.circe.parser
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.io._
import org.specs2.matcher.ThrownMessages
import org.specs2.mutable.Specification
import pdi.jwt.{JwtCirce, JwtClaim}

class AuthenticationTests extends Specification with ThrownMessages {

  World.config = AppConfig.load()

  World.xa = Transactor.fromDriverManager[IO](
    config.dbConfig.driver, config.dbConfig.db, config.dbConfig.user, config.dbConfig.password
  )

  DbSetup.setupDb.transact(xa).unsafeRunSync

  val testUser = UserDAO.findById(1).transact(World.xa).unsafeRunSync.get

  def mkAuthedTestRequest(userId: Long): Request[IO] = {
    val claims = UserClaims(userId)
    val key = World.config.authConfig.key
    val algo = World.config.authConfig.algo
    val now = Instant.now.getEpochSecond

    val claim = JwtClaim(
      issuedAt = Some(now),
      content = claims.asJson.noSpaces
    )

    val token = JwtCirce.encode(claim, key, algo)

    val req = Request[IO](Method.GET)
      .putHeaders(
        headers.Cookie(RequestCookie(World.config.authConfig.cookieName, token))
      )

    req
  }

  "User claims should get encoded/decoded" in {
    val value = UserClaims(42)
    val text = """{"userId":42}"""

    value.asJson.noSpaces should_== text
    parser.decode[UserClaims](text) should_== Right(value)
  }

  "JWT should get decoded" in {

    val claims = UserClaims(42)
    val key = World.config.authConfig.key
    val algo = World.config.authConfig.algo
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

  "Login response should set valid JWT cookie" in {

    val key = World.config.authConfig.key
    val algo = World.config.authConfig.algo
    val claims = UserClaims(testUser.id)

    val res = Authentication.mkUserLoginResponse(testUser)

    val authCookie = res.cookies.find(_.name == World.config.authConfig.cookieName).get

    val tokenJson = JwtCirce.decodeJson(authCookie.content, key, List(algo)).getOrElse(fail("Error decoding token"))

    val claimsDecoded = tokenJson.as[UserClaims].getOrElse(fail("Error reading authUserId from token"))
    claimsDecoded should_== claims

  }

  "Logout response should remove JWT cookie" in {

    val res = Authentication.mkUserLogoutResponse()

    val authCookie = res.cookies.find(_.name == World.config.authConfig.cookieName).get

    authCookie.content should_== ""

  }

  "Authentication should fail for missing JWT cookie" in {

    val req = Request[IO](Method.GET)
    Authentication.readUserClaims(req) should_== Left(ErrorCode.CouldNotFindAuthCookie)

  }

  "Authentication should fail for invalid JWT cookie" in {

    val req = Request[IO](Method.GET)
      .putHeaders(
        headers.Cookie(RequestCookie(World.config.authConfig.cookieName, "invalid-token"))
      )

    Authentication.readUserClaims(req) should_== Left(ErrorCode.CouldNotReadJwtFromAuthCookie)

  }

  "Authentication should fail for JWT cookie with invalid user claims" in {

    val key = World.config.authConfig.key
    val algo = World.config.authConfig.algo

    val claim = JwtClaim(content = "{}")

    val token = JwtCirce.encode(claim, key, algo)

    val req = Request[IO](Method.GET)
      .putHeaders(
        headers.Cookie(RequestCookie(World.config.authConfig.cookieName, token))
      )

    Authentication.readUserClaims(req) should_== Left(ErrorCode.CouldNotReadUserClaimsFromAuthCookie)

  }

  "Authentication should read user claims from Request" in {

    val testUserId = 42
    val req = mkAuthedTestRequest(testUserId)

    val res = Authentication.readUserClaims(req)
    res should_== Right(UserClaims(testUserId))

    val res2 = Authentication.authenticate(req).unsafeRunSync()
    res2 should_== UserClaims(testUserId)

  }

  "Authentication should fetch user from Request" in {

    val req = mkAuthedTestRequest(testUser.id)

    val res = Authentication.fetchUser(req).unsafeRunSync()
    res should_== Right(testUser)

    val res2 = Authentication.authenticateUser(req).unsafeRunSync()
    res2 should_== testUser

    val res3 = Authentication.loadUser(req) { user =>
      user should_== testUser
      Ok()
    }.unsafeRunSync()

    res3.status should_== Status.Ok

  }

}
