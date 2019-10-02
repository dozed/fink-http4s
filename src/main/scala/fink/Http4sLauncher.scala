package fink

import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s.server.Router
import scala.concurrent.ExecutionContext
import cats.implicits._
import cats.effect._
import doobie.implicits._
import doobie._

import fink.data._
import fink.data.JsonInstances._
import fink.data.Operation
import fink.db.{PostDAO, xa}

object Http4sLauncher extends App {


  implicit val createPostEntityDecoder: EntityDecoder[IO, Operation.CreatePost] = jsonOf[IO, Operation.CreatePost]
  implicit val createdPostEntityEncoder: EntityEncoder[IO, Notification.CreatedPost] = jsonEncoderOf[IO, Notification.CreatedPost]

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)



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

      val author = User(1, "foo", "bar")

      req.as[Operation.CreatePost].flatMap { op =>

        PostDAO.create(op.title, op.text, author, op.tags).transact(xa)

      }.flatMap { postInfo =>

        val msg = Notification.CreatedPost(postInfo)

        Ok(msg)
      }


  }


  val httpApp = Router("/" -> adminService, "/api" -> apiService).orNotFound

  val serverBuilder = BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)

  serverBuilder.serve.compile.drain.as(ExitCode.Success).unsafeRunSync()


}
