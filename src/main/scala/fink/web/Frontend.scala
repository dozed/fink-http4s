//package fink.web
//
//import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
//import fink.support.CatsSupport
//import slick.driver.H2Driver.api._
//
//import fink.World
//import fink.data._
//
//import org.scalatra.scalate.ScalateSupport
//import org.scalatra._
//
//import org.joda.time._
//import org.joda.time.format._
//
//import fink.data.Queries._
//
//import scala.concurrent.Future
//import scalaz._, Scalaz._
//
//class Frontend(val world: World) extends ScalatraServlet with ResourceRoutes with ApiFormats with ScalateSupport with FutureSupport with CatsSupport {
//
//  implicit val executor = scala.concurrent.ExecutionContext.Implicits.global
//
//  override implicit protected val jsonFormats = JsonFormats()
//
//  override def jade(template: String, attributes: (String, Any)*)(implicit request: HttpServletRequest, response: HttpServletResponse) = {
//    templateAttributes.put("layout", "/frontend/layouts/default.jade")
//    super.jade("/frontend/%s.jade".format(template), attributes:_*)
//  }
//
//  before() {
//    contentType = formats("html")
//  }
//
//  get("/") {
//    new AsyncResult {
//      val is = {
//
//        world.db.run(posts.sortBy(_.date).result) map { posts =>
//          Ok(jade("index", "posts" -> posts))
//        }
//
//      }
//    }
//  }
//
//  get("/posts/:year/:month/:day/:shortlink/?") {
//    new AsyncResult {
//      val is = {
//
//        (for {
//          year <- params.getAs[Int]("year") \/> BadRequest()
//          month <- params.getAs[Int]("month") \/> BadRequest()
//          day <- params.getAs[Int]("day") \/> BadRequest()
//          shortlink = params("shortlink")
//        } yield {
//
//          world.db.run(posts.byShortlink(shortlink).result.headOption) map {
//            _.fold(NotFound())(post => Ok(jade("post", "post" -> post)))
//          }
//
//        }).sequence[Future, ActionResult]
//
//      }
//    }
//  }
//
//  get("/posts/:year/:month/?") {
//    new AsyncResult {
//      val is = {
//
//        (for {
//          year <- params.getAs[Int]("year") \/> BadRequest()
//          month <- params.getAs[Int]("month") \/> BadRequest()
//        } yield {
//
//          world.db.run(posts.byMonth(month, year).result) map { post =>
//            val date = new LocalDate(year, month, 1)
//            val formatter = DateTimeFormat.forPattern("MMMM")
//
//            Ok(jade("archive-month", "posts" -> posts, "year" -> year, "month" -> formatter.print(date)))
//          }
//
//        }).sequence[Future, ActionResult]
//
//      }
//    }
//  }
//
//  //  get("/tag/:tag/?") {
//  //    val tag = params("tag")
//  //    postRepository.byTag(tag) match {
//  //      case Nil => halt(404, "Not found.")
//  //      case posts: List[_] => jade("archive-tag", "posts" -> posts, "tag" -> tag)
//  //    }
//  //  }
//  //
//  //  get("/page/:shortlink") {
//  //    val shortlink = params("shortlink")
//  //
//  //    pageRepository.byShortlink(shortlink) match {
//  //      case Some(page) => jade("page", "page" -> page)
//  //      case None => halt(404, "Not found.")
//  //    }
//  //  }
//  //
//  //  get("/media/:shortlink") {
//  //    galleryRepository.byShortlink(params("shortlink")) match {
//  //      case Some(gallery) => jade("album", "gallery" -> gallery)
//  //      case None => halt(404, "Not found.")
//  //    }
//  //  }
//
//  notFound {
//    contentType = null
//    serveStaticResource() getOrElse halt(404, "Not found.")
//  }
//
//}
