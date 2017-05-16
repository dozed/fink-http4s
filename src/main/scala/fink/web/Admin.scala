//package fink.web
//
//import fink.data._
//import org.scalatra.json.JacksonJsonSupport
//import org.scalatra.scalate.ScalateSupport
//import org.scalatra.{Found, NotFound, ScalatraServlet}
//
//class Admin extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {
//
//  override val jsonFormats = JsonFormats()
//
//  override val isScalateErrorPageEnabled = false
//
//  before("""/api/.+""".r) {
//    contentType = formats("json")
//  }
//
//  get("/") {
//    if (request.getPathInfo == null) {
//      Found(url("/") + "/")
//    } else {
//      contentType = formats("html")
//      Views.admin.toString
//    }
//  }
//
//  notFound {
//    contentType = null
//    serveStaticResource() getOrElse NotFound()
//  }
//
//}
