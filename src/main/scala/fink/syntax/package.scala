package fink

import cats.effect.IO
import fink.data.{User, UserClaims}
import fink.auth.Authentication
import org.http4s.Request

package object syntax {

  implicit class AuthenticateSyntax(val req: Request[IO]) extends AnyVal {
    def authenticate: IO[UserClaims] = Authentication.authenticate(req)
    def authenticateUser: IO[User] = Authentication.authenticateUser(req)
  }

}
