package fink

import cats.effect.IO
import doobie.Transactor
import doobie.implicits._
import fink.World.{config, cs, xa}
import fink.data._
import fink.db.{DbSetup, ImageDAO, PostDAO, TagDAO, UserDAO}
import org.http4s.MediaType
import org.mindrot.jbcrypt.BCrypt
import org.specs2.mutable._

class DataSpec extends Specification {

  sequential

  World.config = AppConfig.load()

  World.xa = Transactor.fromDriverManager[IO](
    config.dbConfig.driver, config.dbConfig.db, config.dbConfig.user, config.dbConfig.password
  )

  DbSetup.setupDb.transact(xa).unsafeRunSync()

  // load users for testing
  val author = UserDAO.findById(1).transact(xa).unsafeRunSync().get


  "should create tag" in {
    val fooTag = TagDAO.create("foo").transact(xa).unsafeRunSync()
    TagDAO.create("bar").transact(xa).unsafeRunSync()
    TagDAO.create("baz").transact(xa).unsafeRunSync()

    // TagDAO.findAll.transact(xa).unsafeRunSync must have size (3)
    TagDAO.findById(fooTag.id).transact(xa).unsafeRunSync() must beSome.which(_.value.equals("foo"))
    TagDAO.findById(2).transact(xa).unsafeRunSync() must beSome.which(_.id == 2)
    TagDAO.findById(20).transact(xa).unsafeRunSync() must beNone
  }

  "should create user" in {

    UserDAO.findAll

    val user = UserDAO.create("name", "password", Set(UserRole.CanEdit)).transact(xa).unsafeRunSync()
    user.roles should_== Set(UserRole.CanEdit)

    user.name should_== "name"
    BCrypt.checkpw("password", user.password) should beTrue

    UserDAO.findAll.transact(xa).unsafeRunSync() must have size (2)

    val user1 = UserDAO.findById(2).transact(xa).unsafeRunSync()
    user1 should_== Some(user)

  }

  "should create post" in {

    val post = PostDAO.create(mkTime, "title", author.id, "title", "value").transact(xa).unsafeRunSync()

    PostDAO.addTag(post.id, "foox").transact(xa).unsafeRunSync()

    val xs = PostDAO.findTags(post.id).transact(xa).unsafeRunSync()
    xs.size should_== 1
    xs.head.value should_== "foox"

    val post1 = PostDAO.findById(post.id).transact(xa).unsafeRunSync().get
    post1 should_== post

    val xs1 = PostDAO.findAll.transact(xa).unsafeRunSync()
    xs1 should contain(post1)

    val post2 = PostDAO.findPostInfoById(post.id).transact(xa).unsafeRunSync().get
    post2.post should_== post
    post2.tags should_== xs

  }

  "should create and retrieve images" in {

    val imgInfo = ImageDAO.create("title", "hash", "ext", MediaType.image.png, "fileName", author).transact(xa).unsafeRunSync()

    val img = ImageDAO.findById(imgInfo.image.id).transact(xa).unsafeRunSync()

    img should_== Some(imgInfo.image)

    val imgs = ImageDAO.findAll.transact(xa).unsafeRunSync()

    imgs should contain(imgInfo.image)

  }

}