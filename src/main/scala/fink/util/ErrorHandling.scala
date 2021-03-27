package fink.util

import cats.ApplicativeThrow
import cats.data.Kleisli
import cats.syntax.applicativeError._
import org.http4s.{Http, Request, Response}

object ErrorHandling {
  def apply[F[_], G[_]](http: Http[F, G], errorHandler: Request[G] => PartialFunction[Throwable, F[Response[G]]])
    (implicit F: ApplicativeThrow[F]): Http[F, G] =
    Kleisli { req =>
      http(req).recoverWith(errorHandler(req))
    }
}
