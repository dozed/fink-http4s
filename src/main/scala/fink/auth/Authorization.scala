package fink.auth

import cats.effect.IO
import fink.data.{ErrorCode, User, UserRole}

object Authorization {

  def authorizeEdit(user: User): IO[Unit] = {
    if (user.roles.contains(UserRole.CanEdit)) IO.pure(())
    else IO.raiseError(ErrorCode.NotAuthorized)
  }

}
