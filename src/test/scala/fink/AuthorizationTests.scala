package fink

import cats.effect.IO
import doobie.Transactor
import doobie.implicits._
import fink.World.{config, cs, xa}
import fink.auth.Authorization
import fink.data.{AppConfig, ErrorCode, User, UserRole}
import fink.db.DbSetup
import org.specs2.matcher.ThrownMessages
import org.specs2.mutable.Specification

class AuthorizationTests extends Specification with ThrownMessages {

  World.config = AppConfig.load()

  World.xa = Transactor.fromDriverManager[IO](
    config.dbConfig.driver, config.dbConfig.db, config.dbConfig.user, config.dbConfig.password
  )

  DbSetup.setupDb.transact(xa).unsafeRunSync

  val testUserWithEditRole = User(2, "foo", "bar", Set(UserRole.CanEdit))
  val testUserWithoutRoles = User(2, "foo", "bar", Set.empty)

  "User without role can not edit" in {

    Authorization.authorizeEdit(testUserWithoutRoles).attempt.unsafeRunSync() should_== Left(ErrorCode.NotAuthorized)

  }

  "User with edit role can edit" in {

    Authorization.authorizeEdit(testUserWithEditRole).attempt.unsafeRunSync() should_== Right(())

  }


}
