//package fink.web
//
//import scalatags.Text.all._
//
//object Views {
//
//  def admin = {
//
//    html(lang := "en", "ng-app".attr := "fink-admin",
//      head(
//        meta(charset := "utf-8"),
//        "title".tag.apply("Fink admin"),
//
//        link(rel := "stylesheet", tpe := "text/css", href := "./lib/bootstrap/bootstrap.min.css"),
//        link(rel := "stylesheet", tpe := "text/css", href := "./lib/uploadify-3.1.1/uploadify.css"),
//        link(rel := "stylesheet", tpe := "text/css", href := "./css/site.css"),
//
//        script(tpe := "text/javascript", src := "./lib/underscore/underscore-1.4.4.min.js"),
//        script(tpe := "text/javascript", src := "//code.jquery.com/jquery-1.10.2.js"),
//        script(tpe := "text/javascript", src := "//code.jquery.com/ui/1.11.4/jquery-ui.js"),
//        script(tpe := "text/javascript", src := "./lib/uploadify-3.1.1/jquery.uploadify-3.1.min.js"),
//        script(tpe := "text/javascript", src := "./lib/angular/angular.min.js"),
//        script(tpe := "text/javascript", src := "./lib/angular/angular-resource.min.js"),
//        script(tpe := "text/javascript", src := "./lib/bootstrap/bootstrap.min.js"),
//        script(tpe := "text/javascript", src := "./lib/bootbox/bootbox.min.js"),
//        script(tpe := "text/javascript", src := "./lib/ace/ace.js"),
//
//        script(tpe := "text/javascript", src := "./js/app.js"),
//        script(tpe := "text/javascript", src := "./js/services.js"),
//        script(tpe := "text/javascript", src := "./js/controllers.js"),
//        script(tpe := "text/javascript", src := "./js/filters.js"),
//        script(tpe := "text/javascript", src := "./js/directives.js")
//
//      ),
//      body(
//        div(
//          cls := ".sidebar",
//          h4("CONTENT"),
//          ul(
//            cls := "menu",
//            li(a(cls := ".posts", href := "#/posts", "posts")),
//            li(a(cls := ".pages", href := "#/pages", "pages")),
//            li(a(cls := ".albums", href := "#/galleries", "galleries")),
//            li(a(cls := ".settings", href := "#/settings", "settings"))
//          )
//        ),
//        div(cls := ".content-wrap",
//          div(cls := ".header"),
//          div(cls := ".content",
//            div(cls := ".container", "ng-view".attr := "", style := "margin: 0;")
//          )
//        )
//      )
//    )
//
//
//  }
//
//}
