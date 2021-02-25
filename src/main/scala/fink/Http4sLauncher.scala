package fink

import cats.data.Kleisli
import cats.effect._
import doobie._
import fink.World._
import fink.data._
import fink.web._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext

object Http4sLauncher extends App {

  World.config = AppConfig.load()

  World.xa = Transactor.fromDriverManager[IO](
    config.dbConfig.driver, config.dbConfig.db, config.dbConfig.user, config.dbConfig.password
  )

  val httpApp = Router(
    "/api" -> PostApi.routes,
    "/api" -> PageApi.routes,
    "/api" -> GalleryApi.routes,
    "/api" -> ImageApi.routes,
    "/api" -> TagApi.routes,
    "/api/auth" -> AuthApi.routes,
  ).orNotFound

  object ErrorCodeMiddleware {
    def apply(app: HttpApp[IO]): HttpApp[IO] = Kleisli { req =>
      app(req).handleErrorWith {
        case ErrorCode.NotAuthenticated => Forbidden()
        case ErrorCode.InvalidRequest => BadRequest()
        case e => IO.raiseError(e)
      }
    }
  }

  val serverBuilder = BlazeServerBuilder.apply[IO](ExecutionContext.global)
    .bindHttp(World.config.port, World.config.host)
    .withHttpApp(ErrorCodeMiddleware(httpApp))

  serverBuilder
    .serve.compile.drain
    .as(ExitCode.Success)
    .unsafeRunSync()


}
