package io.github.ilinandrii

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import zio.console.Console
import zio.{Has, RLayer, Task, ZIO, ZManaged}
import scala.concurrent.duration.DurationInt

trait HttpServer {
  def launch(host: String, port: Int, route: Route): Task[Unit]
}

object HttpServer {
  def launch(host: String, port: Int, route: Route) =
    ZIO.serviceWith[HttpServer](_.launch(host, port, route))

  val live: RLayer[Console, Has[HttpServer]] = {
    ZIO.service[Console.Service].map(console => HttpServerLive(console)).toLayer
  }

  case class HttpServerLive(console: Console.Service) extends HttpServer {

    private def startSystem = ZIO(ActorSystem("akka-http-system")).tap { system =>
      console.putStrLn(s"Started an actor system: ${system.name}")
    }

    private def terminateSystem(system: ActorSystem) =
      (ZIO.fromFuture(implicit ec => system.terminate()) *> ZIO.fromFuture(_ => system.whenTerminated))
        .tapBoth(
          exception => console.putStrLnErr(s"Unable to terminate an actor system. Exception: ${exception.getMessage}"),
          _ => console.putStrLn("Successfully terminated an actor system")
        )
        .orDie

    private def startServer(host: String, port: Int, route: Route)(implicit system: ActorSystem) =
      ZIO.fromFuture { implicit ec =>
        Http()
          .newServerAt(host, port)
          .bind(route)
          .map(_.addToCoordinatedShutdown(30.seconds))
      }.zipLeft(console.putStrLn(s"Started an HTTP server at $host:$port"))

    override def launch(host: String, port: Int, route: Route) = (for {
      system <- ZManaged.make(startSystem)(terminateSystem)
      _      <- ZManaged.fromEffect(startServer(host, port, route)(system))
    } yield ()).useForever.orDie.unit

  }
}


