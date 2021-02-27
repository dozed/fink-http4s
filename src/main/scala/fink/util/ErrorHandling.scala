package fink.util

import cats.{ApplicativeError, ApplicativeThrow, Apply}
import cats.data.Kleisli
import cats.syntax.applicativeError._
import org.http4s.{Http, Response}

object ErrorHandling {
  def apply[F[_], G[_]](http: Http[F, G], errorHandler: PartialFunction[Throwable, F[Response[G]]])
    (implicit F: ApplicativeThrow[F]): Http[F, G] =
    Kleisli { req =>
      http(req).recoverWith(errorHandler)
    }
}

trait Foo[F[_]] extends ApplicativeError[F, Throwable] {

  def onError1[A](fa: F[A])(pf: PartialFunction[Throwable, F[Unit]]): F[A] =
    handleErrorWith(fa) { e =>
      pf.andThen(fu => map2(fu, raiseError[A](e))((_, b) => b))
        .applyOrElse(e, raiseError[A])
    }

}