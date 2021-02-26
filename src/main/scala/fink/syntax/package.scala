package fink

import cats.effect.IO
import fink.data.{User, UserClaims}
import fink.modules.AuthModule
import org.http4s.Request

package object syntax {

  implicit class AuthenticateSyntax(val req: Request[IO]) extends AnyVal {
    def authenticate: IO[UserClaims] = AuthModule.authenticate(req)
    def authenticateUser: IO[User] = AuthModule.authenticateUser(req)
  }

}
