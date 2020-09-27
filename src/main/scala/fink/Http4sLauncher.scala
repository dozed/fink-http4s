package fink

import cats.effect._
import doobie._
import fink.World._
import fink.data._
import fink.web._
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
    // "/assets" -> fileService[IO](FileService.Config("./assets")),
  ).orNotFound
//    .handleError {
//      case ErrorCode.P
//    }

  val serverBuilder = BlazeServerBuilder.apply[IO](ExecutionContext.global)
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)

  serverBuilder
    .serve.compile.drain
    .as(ExitCode.Success)
    .unsafeRunSync()


}
