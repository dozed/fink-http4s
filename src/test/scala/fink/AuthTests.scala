package fink

import java.time.Instant

import pdi.jwt.{Jwt, JwtAlgorithm, JwtCirce, JwtClaim}

import cats.implicits._

object AuthTests extends App {

  val claim = JwtClaim(
    issuedAt = Some(Instant.now.getEpochSecond),
    content = """{"authUserId":42}"""
  )

  val key = "secretK3y"
  val algo = JwtAlgorithm.HS256
  val token = JwtCirce.encode(claim, key, algo)

  println(token)

  val res = JwtCirce.decodeJson(token, key, Seq(JwtAlgorithm.HS256)).toEither
  println(res)

  val res1 = res.fold(_ => (), _.hcursor.downField("authUserId").as[Long])
  println(res1)

  // Jwt.validate()


}
