package fink

import cats.effect.IO
import fink.data.User
import fink.modules.AuthModule
import org.http4s.Request

package object syntax {

  implicit class AuthenticateSyntax(val req: Request[IO]) extends AnyVal {
    def authenticateUser: IO[User] = AuthModule.authenticateUser(req)
  }

}
