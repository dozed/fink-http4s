package fink.db

import fink.data._

import doobie.imports._
import cats.implicits._


object DbSetup extends App {


  val defaultTags: List[Tag] =
    List(
      Tag(1, "Music"),
      Tag(2, "Programming"),
      Tag(3, "Art")
    )

  val defaultUsers: List[User] =
    List(
      User(1, "foo", mkPassword("bar"))
    )

  val defaultPosts: List[Post] =
    List(
      Post(1, mkTime, "foo", 1, "foo-bar", "42 foo bars")
    )


  val drop: ConnectionIO[Int] = Update0(scala.io.Source.fromFile("./sql/drop.sql").mkString, None).run

  val create: ConnectionIO[Int] = Update0(scala.io.Source.fromFile("./sql/create.sql").mkString, None).run

  val setupDb: ConnectionIO[Unit] = {
    for {
      _ <- drop
      _ <- create
      _ <- defaultTags.map(t => DAO.createTag(t.id, t.value)).sequence
      _ <- defaultUsers.map(u => DAO.createUser(u.id, u.name, u.password)).sequence
      _ <- defaultPosts.map(p => DAO.createPost(p.id, p.date, p.title, p.authorId, p.shortlink, p.text)).sequence
    } yield {
      ()
    }
  }




  val xa = DriverManagerTransactor[IOLite](
    "org.postgresql.Driver", "jdbc:postgresql:fink", "postgres", ""
  )

  setupDb.transact(xa).unsafePerformIO

}
