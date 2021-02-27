package fink

import fink.auth.Authorization
import fink.data.{ErrorCode, User, UserRole}
import org.specs2.matcher.ThrownMessages
import org.specs2.mutable.Specification

class AuthorizationTests extends Specification with ThrownMessages {

  val testUserWithEditRole = User(2, "foo", "bar", Set(UserRole.CanEdit))
  val testUserWithoutRoles = User(2, "foo", "bar", Set.empty)

  "User without role can not edit" in {

    Authorization.authorizeEdit(testUserWithoutRoles).attempt.unsafeRunSync() should_== Left(ErrorCode.NotAuthorized)

  }

  "User with edit role can edit" in {

    Authorization.authorizeEdit(testUserWithEditRole).attempt.unsafeRunSync() should_== Right(())

  }


}
