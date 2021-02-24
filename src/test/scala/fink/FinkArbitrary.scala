package fink

import fink.data.{Page, Post, UnixTime}
import io.circe.testing.ArbitraryInstances
import org.scalacheck.Arbitrary

object FinkArbitrary extends ArbitraryInstances {

  implicit val postArbitrary: Arbitrary[Post] = Arbitrary {
    for {
      id <- Arbitrary.arbitrary[Long]
      date <- Arbitrary.arbitrary[UnixTime]
      title <- Arbitrary.arbitrary[String]
      authorId <- Arbitrary.arbitrary[Long]
      shortlink <- Arbitrary.arbitrary[String]
      text <- Arbitrary.arbitrary[String]
    } yield {
      Post(id, date, title, authorId, shortlink, text)
    }
  }

  implicit val pageArbitrary: Arbitrary[Page] = Arbitrary {
    for {
      id <- Arbitrary.arbitrary[Long]
      date <- Arbitrary.arbitrary[UnixTime]
      title <- Arbitrary.arbitrary[String]
      authorId <- Arbitrary.arbitrary[Long]
      shortlink <- Arbitrary.arbitrary[String]
      text <- Arbitrary.arbitrary[String]
    } yield {
      Page(id, date, title, authorId, shortlink, text)
    }
  }

}
