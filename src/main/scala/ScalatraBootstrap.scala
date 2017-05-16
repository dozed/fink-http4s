
import fink._
import fink.data._

import org.scalatra._
import javax.servlet.ServletContext
import doobie.imports._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ScalatraBootstrap extends LifeCycle {

  val xa = DriverManagerTransactor[IOLite](
    "org.postgresql.Driver", "jdbc:postgresql:fink", "postgres", ""
  )

  val config = AppConfig.load
  val world = World(xa, config)

//  val frontend = new Frontend(world)

  override def init(context: ServletContext) {
//    context.mount(frontend, "/*")
//    context.mount(new Admin, "/admin/*")
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
  }
}

