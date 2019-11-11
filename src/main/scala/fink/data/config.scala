package fink.data

import com.typesafe.config.ConfigFactory

case class AppConfig(
  port: Int,
  webBase: String,
  dataDirectory: String,
  env: AppEnvironment,
  mailConfig: MailConfig,
  dbConfig: DatabaseConfig,
) {

  def isProduction = env == AppEnvironment.Production
  def isDevelopment = env == AppEnvironment.Development

  val uploadDirectory: String = s"$dataDirectory/uploads"

  val resourceDirectory: String = s"$dataDirectory/resources"

  val publicDirectory: String = s"$dataDirectory/public"


}

case class MailConfig(
  user: String,
  password: String,
  host: String,
  sender: String
)

case class DatabaseConfig(
  driver: String,
  db: String,
  user: String,
  password: String,
)

sealed trait AppEnvironment

object AppEnvironment {

  case object Development extends AppEnvironment
  case object Staging extends AppEnvironment
  case object Test extends AppEnvironment
  case object Production extends AppEnvironment

  def fromString(s: String): AppEnvironment = {
    s match {
      case "development" => AppEnvironment.Development
      case "staging" => AppEnvironment.Staging
      case "test" => AppEnvironment.Test
      case "production" => AppEnvironment.Production
    }
  }

  def asString(s: AppEnvironment): String = {
    s match {
      case AppEnvironment.Development => "development"
      case AppEnvironment.Staging => "staging"
      case AppEnvironment.Test => "test"
      case AppEnvironment.Production => "production"
    }
  }
}

object AppConfig {
  def load(): AppConfig = {
    val cfg = ConfigFactory.load

    val dataDirectory = cfg.getString("dataDirectory")
    val webBase = cfg.getString("webBase")
    val port = cfg.getInt("port")
    val env = AppEnvironment.fromString(cfg.getString("environment"))
    val mailConfig = MailConfig(
      cfg.getString("email.user"),
      cfg.getString("email.password"),
      cfg.getString("email.host"),
      cfg.getString("email.sender"))

    val dbConfig = DatabaseConfig(
      cfg.getString("database.driver"),
      cfg.getString("database.db"),
      cfg.getString("database.user"),
      cfg.getString("database.password"))

    AppConfig(port, webBase, dataDirectory, env, mailConfig, dbConfig)
  }
}