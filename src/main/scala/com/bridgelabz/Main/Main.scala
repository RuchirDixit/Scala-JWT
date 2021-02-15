// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.bridgelabz.Main

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.bridgelabz.Main.actorsystem.ActorSystemFactory
import com.bridgelabz.Main.routes.UserManagementRoutes
import com.bridgelabz.Main.services.UserManagementService
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
// $COVERAGE-OFF$
object Main extends App with LazyLogging {
  val conf = ConfigFactory.load()
  val host = sys.env("Host")
    val port_number = sys.env("Port_number").toInt
  implicit val actorSystem = ActorSystemFactory.system
  implicit val materializer: ActorMaterializer =
    ActorMaterializer()(actorSystem)
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  val userManagementService = new UserManagementService
  val userManagementRoutes = new UserManagementRoutes(userManagementService)
  val routes = userManagementRoutes.routes

  val bindingFuture = Http().bindAndHandle(routes, host, port_number)
  logger.info(s"Server online at http://" + host + ":" + port_number)
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())
}
