package io.github.ilinandrii

import akka.http.scaladsl.server.RouteConcatenation._
import sttp.tapir.Schema
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.generic.Configuration
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.swagger.akkahttp.SwaggerAkka
import zio.App
import zio.console.{Console, putStrLn}
import zio.magic._


object Main extends App {
  val program = for {
    userRoute     <- UserController.userRoute
    userEndpoints <- UserController.userEndpoints
    docs           = OpenAPIDocsInterpreter().toOpenAPI(userEndpoints, "User API", "1.0")
    swaggerRoute   = new SwaggerAkka(docs.toYaml, "swagger", "swagger.yaml").routes
    _             <- HttpServer.launch("localhost", 8080, userRoute ~ swaggerRoute)
  } yield ()

  override def run(args: List[String]) =
    program.inject(Console.live, HttpServer.live, UserController.live).exitCode
}
