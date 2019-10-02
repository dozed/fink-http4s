package fink

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._

import scala.concurrent.ExecutionContext
import cats.implicits._
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s.server.Router
import doobie.implicits._
import doobie._

import fink.data._
import fink.data.JsonInstances._
import fink.data.Operation
import fink.db.DAO

object Http4sLauncher extends App {


  implicit val createPostEntityDecoder: EntityDecoder[IO, Operation.CreatePost] = jsonOf[IO, Operation.CreatePost]
  implicit val createdPostEntityEncoder: EntityEncoder[IO, Notification.CreatedPost] = jsonEncoderOf[IO, Notification.CreatedPost]

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  // implicit val cs = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:fink", "fink", "fink"
  )



  val adminService = HttpRoutes.of[IO] {
    case GET -> Root / "admin" / "index" =>
      Ok(s"Hello, name.")

    case GET -> Root / "admin" / "posts" =>
      Ok(s"Hello, name.")
  }

  val apiService = HttpRoutes.of[IO] {
    case GET -> Root / "posts" =>
      Ok(s"Hello, name.")

    case req@POST -> Root / "posts" =>

      val author = User(0, "foo", "bar")

      req.as[Operation.CreatePost].flatMap { op =>
        val shortlink = op.title.toLowerCase.replaceAllLiterally(" ", "-")
        val time = mkTime
        DAO.createPost(time, op.title, 0, shortlink, op.text).transact(xa)
      }.flatMap { post =>

        val msg = Notification.CreatedPost(post, author)

        Ok(msg)
      }


  }


  // val services =  adminService <+> apiService
  // services: cats.data.Kleisli[[β$0$]cats.data.OptionT[cats.effect.IO,β$0$],org.http4s.Request[cats.effect.IO],org.http4s.Response[cats.effect.IO]] = Kleisli(cats.data.KleisliSemigroupK$$Lambda$19322/1054463322@67af20e7)

  val httpApp = Router("/" -> adminService, "/api" -> apiService).orNotFound
  // httpApp: cats.data.Kleisli[cats.effect.IO,org.http4s.Request[cats.effect.IO],org.http4s.Response[cats.effect.IO]] = Kleisli(org.http4s.syntax.KleisliResponseOps$$Lambda$19333/161462022@3615114a)

  val serverBuilder = BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)

  // val fiber = serverBuilder.resource.use(_ => IO.never).start.unsafeRunSync()
  serverBuilder.serve.compile.drain.as(ExitCode.Success).unsafeRunSync()


}
