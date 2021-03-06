package org.timspence.scalaplayground.http4s

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._

import scala.concurrent.ExecutionContext.global

object HelloWorldServer extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    ServerStream.stream[IO].compile.drain.as(ExitCode.Success)
}

object ServerStream {
  def helloWorldRoutes[F[_]: Effect]: HttpRoutes[F] = new HelloWorldRoutes[F].routes

  def stream[F[_]: ConcurrentEffect: Timer]: fs2.Stream[F, ExitCode]=
    BlazeServerBuilder[F](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(Router(
        "/" -> helloWorldRoutes
      ).orNotFound)
      .serve
}
