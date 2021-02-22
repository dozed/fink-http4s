package fink

import cats.effect._
import cats.syntax.applicative._
import doobie._
import fink.World._
import fink.data._
import fink.web._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.log4s.getLogger

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

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

  val messageFailureLogger = getLogger("org.http4s.server.message-failures")
  val serviceErrorLogger = getLogger("org.http4s.server.service-errors")

  val serviceErrorHandler: Request[IO] => PartialFunction[Throwable, IO[Response[IO]]] =
    req => {
      case ErrorCode.AuthenticationError => Forbidden()
      case mf: MessageFailure =>
        messageFailureLogger.debug(mf)(
          s"""Message failure handling request: ${req.method} ${req.pathInfo} from ${req.remoteAddr
            .getOrElse("<unknown>")}""")
        mf.toHttpResponse[IO](req.httpVersion).pure[IO]
      case NonFatal(t) =>
        serviceErrorLogger.error(t)(
          s"""Error servicing request: ${req.method} ${req.pathInfo} from ${req.remoteAddr
            .getOrElse("<unknown>")}""")
        IO.pure(
          Response(
            Status.InternalServerError,
            req.httpVersion,
            Headers(
              Connection("close".ci) ::
                `Content-Length`.zero ::
                Nil
            )))
    }

  val serverBuilder = BlazeServerBuilder.apply[IO](ExecutionContext.global)
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)
    .withServiceErrorHandler(serviceErrorHandler)

  serverBuilder
    .serve.compile.drain
    .as(ExitCode.Success)
    .unsafeRunSync()


}
