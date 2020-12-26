package com.bridgelabz.Main
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import courier._
import Defaults._
import javax.mail.internet.InternetAddress

import scala.util.{Failure, Success}
class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport with LazyLogging {
  val routes: Route =
    pathPrefix("user") {
      // for login using post request, returns success on successful login or else returns unauthorized
      path("login") {
        (post & entity(as[Request])) { loginRequest =>
          logger.info("Login response: " + service.userLogin(loginRequest))
          if (service.userLogin(loginRequest) == "Login Successful") {
            complete((StatusCodes.OK, "Successfully logged in!"))
          } else {
            complete(StatusCodes.Unauthorized,"Invalid credentials. User not found!")
          }
        }
      } ~
        // to register user using post request, returns success on successful registration or else returns Cannot registered
        path("register") {
          (post & entity(as[Request])) { createUserRequest =>
            if (service.createUser(createUserRequest) == "User created") {
              val token: String = TokenAuthorization.generateToken(createUserRequest.email)
              val mailer = Mailer("smtp.gmail.com", 587)
                .auth(true)
                .as(sys.env("sender_email"),sys.env("sender_password"))
                .startTls(true)()
              mailer(Envelope.from(new InternetAddress(sys.env("sender_email")))
                .to(new InternetAddress(createUserRequest.email))
                .subject("Token")
                .content(Text("Thanks for registering! Your token is: " + token))).onComplete {
                case Success(_) => println("Message delivered. Email verified!")
                case Failure(_) => println("Failed to verify user!")
              }
              complete((StatusCodes.OK, "User Registered!"))
            } else {
              complete(StatusCodes.BadRequest -> "Error! User already exists!")
            }
          }
        }
    } ~
      path("protectedcontent") {
        get {
          TokenAuthorization.authenticated { _ =>
            val response = service.protectedContent
            println(response)
            complete(response)
          }
        }
      }
}
