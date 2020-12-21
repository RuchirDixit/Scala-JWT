package com.bridgelabz.Main
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport {
  val routes: Route =
    pathPrefix("user") {
      // for login using post request, returns success on successful login or else returns unauthorized
      path("login") {
        (post & entity(as[Request])) { loginRequest =>
          if (service.userLogin(loginRequest) == "Login Successful") {
            val token = TokenAuthorization.generateToken(loginRequest.email)
            complete((StatusCodes.OK, "Success"))
          } else {
            complete(StatusCodes.Unauthorized)
          }
        }
      } ~
        // to register user using post request, returns success on successful registration or else returns Cannot registered
        path("register") {
          println("Inside register")
          (post & entity(as[Request])) { createUserRequest =>
            if (service.createUser(createUserRequest) == "User Created") {
              val token: String = TokenAuthorization.generateToken(createUserRequest.email)
              println(createUserRequest.email)
              complete((StatusCodes.OK, token))
            } else {
              complete(StatusCodes.BadRequest -> "Cannot Register")
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
