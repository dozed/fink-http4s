package fink.util

import cats.ApplicativeThrow
import cats.data.Kleisli
import cats.syntax.applicativeError._
import org.http4s.{Http, Response}

object ErrorHandling {
  def apply[F[_], G[_]](errorHandler: PartialFunction[Throwable, F[Response[G]]])(http: Http[F, G])
    (implicit F: ApplicativeThrow[F]): Http[F, G] =
    Kleisli { req =>
      http(req).handleErrorWith(e => errorHandler.applyOrElse(e, F.raiseError))
    }
}

