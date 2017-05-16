package fink.support

import org.scalatra._

import cats.implicits._
import cats.data._

trait CatsSupport extends ScalatraBase {

  // handle cats types, basically unwrap the value from the container
  override def renderPipeline: RenderPipeline = ({
    case Right(r) => r
    case Left(l) => l
    case Validated.Valid(r) => r
    case Validated.Invalid(l) => l
    case EitherT(run) => run
  }: RenderPipeline) orElse super.renderPipeline


}
