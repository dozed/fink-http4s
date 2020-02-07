package fink

import cats.effect._
import cats.implicits._
import doobie._
import fink.World._
import fink.data._
import fink.web._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

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
    "/api/auth" -> AuthApi.routes,
    // "/assets" -> fileService[IO](FileService.Config("./assets")),
  ).orNotFound

  val serverBuilder = BlazeServerBuilder[IO]
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)

  serverBuilder
    .serve.compile.drain
    .as(ExitCode.Success)
    .unsafeRunSync()


}
