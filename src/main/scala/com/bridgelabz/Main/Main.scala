package com.bridgelabz.Main
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App {

  val conf = ConfigFactory.load()
  val host = sys.env("Host")
    val port_number = sys.env("Port_number").toInt
  implicit val actorSystem: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer =
    ActorMaterializer()(actorSystem)
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  val userManagementService = new UserManagementService
  val userManagementRoutes = new UserManagementRoutes(userManagementService)
  val routes = userManagementRoutes.routes

  val bindingFuture = Http().bindAndHandle(routes, host, port_number)
  println(s"Server online at http://localhost:8081")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())

}
