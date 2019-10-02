package fink.data

trait Operation

object Operation {

  case class CreatePost(
    title: String,
    text: String
  )

}
