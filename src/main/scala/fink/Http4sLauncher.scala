package fink

import cats.effect._
import doobie._
import fink.World._
import fink.data._
import fink.util.ErrorHandling
import fink.web._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext

object Http4sLauncher extends App {

  World.config = AppConfig.load()

  World.xa = Transactor.fromDriverManager[IO](
    config.dbConfig.driver, config.dbConfig.db, config.dbConfig.user, config.dbConfig.password
  )

  val app: Resource[IO, Server[IO]] =
    for {
      blocker <- Blocker[IO]
      server <- {
        val httpApp = Router(
          "/api" -> PostApi.routes,
          "/api" -> PageApi.routes,
          "/api" -> GalleryApi.routes,
          "/api" -> ImageApi.routes,
          "/api" -> TagApi.routes,
          "/api/auth" -> AuthApi.routes,
          "/" -> ImageService.routes(blocker)
        ).orNotFound

        val httpAppWithErrorHandling =
          ErrorHandling(httpApp, {
            case ErrorCode.NotAuthenticated => Forbidden()
            case ErrorCode.NotAuthorized => Forbidden()
            case ErrorCode.InvalidRequest => BadRequest()
          })

        BlazeServerBuilder[IO](ExecutionContext.global)
          .bindHttp(8080)
          .withHttpApp(httpAppWithErrorHandling)
          .resource
      }
    } yield server

  app.use(_ => IO.never).as(ExitCode.Success).unsafeRunSync()


}
