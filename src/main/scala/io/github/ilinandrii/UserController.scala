package io.github.ilinandrii

import akka.http.scaladsl.server.Route
import cats.implicits.catsSyntaxEitherId
import io.github.ilinandrii.UserController.UserEndpoint
import scala.concurrent.Future
import sttp.tapir.json.circe._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.{Endpoint, endpoint}
import sttp.tapir.generic.auto._
import zio.{Has, UIO, ULayer, URIO, ZIO, ZLayer}

trait UserController {
  def userRoute: UIO[Route]
  def userEndpoints: UIO[Seq[UserEndpoint]]
}

object UserController {
  type UserEndpoint = Endpoint[_, _, _, _]

  def userRoute: URIO[Has[UserController], Route]                 = ZIO.serviceWith(_.userRoute)
  def userEndpoints: URIO[Has[UserController], Seq[UserEndpoint]] = ZIO.serviceWith(_.userEndpoints)

  val live: ULayer[Has[UserController]] = ZLayer.succeed(UserControllerLive())

  case class UserControllerLive() extends UserController {
    override def userRoute     = UIO(AkkaHttpServerInterpreter().toRoute(createUser))
    override def userEndpoints = UIO(Seq(createUser.endpoint))

    private def createUser = endpoint
      .name("createUser")
      .post
      .in("user")
      .in(jsonBody[User])
      .out(jsonBody[User])
      .serverLogic(createUserLogic)

    private lazy val createUserLogic = (user: User) => Future.successful(user.asRight[Unit])
  }
}
