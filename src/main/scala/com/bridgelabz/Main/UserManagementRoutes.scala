package com.bridgelabz.Main
// TODO : Use UpdateOne method to update isVerified key to true once user verified
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import courier._
import Defaults._
import javax.mail.internet.InternetAddress
import com.nimbusds.jose.JWSObject
import org.mongodb.scala.model.Filters._
import org.bson.{BsonDocument, BsonType}
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates._

import concurrent.duration._
import scala.concurrent.Await
import scala.util.{Failure, Success}
class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport with LazyLogging {
  val routes: Route =
    pathPrefix("user") {
      // for login using post request, returns success on successful login or else returns unauthorized
      path("login") {
        (post & entity(as[User])) { loginRequest =>
          logger.info("Login response: " + service.userLogin(loginRequest))
          println("Login res::::"+service.userLogin(loginRequest))
          if (service.userLogin(loginRequest) == "Login Successful") {
            complete((StatusCodes.OK, "Successfully logged in!"))
          }
          else if (service.userLogin(loginRequest) == "User Not verified") {
            complete(StatusCodes.UnavailableForLegalReasons,"User's Email Id is not verified!")
          }
          else {
            complete(StatusCodes.Unauthorized,"Invalid credentials. User not found!")
          }
        }
      } ~
      // to verify whether user's email id exists or not. If user reaches this path that means his email id is authentic.
      path("verify"){
        get {

          parameters('token.as[String],'email.as[String]) {
            (token,email) =>
              val jwsObject = JWSObject.parse(token)
              val updateUserAsVerified = MongoDatabase.collection.updateOne(equal("email",email),set("isVerified",true)).toFuture()
              Await.result(updateUserAsVerified,60.seconds)
              if(jwsObject.getPayload.toJSONObject.get("email").equals(email)){
                complete("User successfully verified and registered!")
              }
              else {
                complete("User could not be verified!")
              }
          }
        }
      } ~
        // to register user using post request, returns success on successful registration or else returns Cannot registered
        path("register") {
          (post & entity(as[User])) { createUserRequest =>
            if (service.createUser(createUserRequest) == "User created") {
              val token: String = TokenAuthorization.generateToken(createUserRequest.email)
              val mailer = Mailer("smtp.gmail.com", 587)
                .auth(true)
                .as(sys.env("sender_email"),sys.env("sender_password"))
                .startTls(true)()
              mailer(Envelope.from(new InternetAddress(sys.env("sender_email")))
                .to(new InternetAddress(createUserRequest.email))
                .subject("Token")
                .content(Text("Thanks for registering! Click on this link to verify your email address: http://localhost:8081/user/verify?token="
                  +token+"&email="+createUserRequest.email)))
                .onComplete {
                case Success(_) => println("Message delivered. Email verified!")
                case Failure(_) => println("Failed to verify user!")
              }
              complete((StatusCodes.OK, "User Registered!"))
            }
            else if (service.createUser(createUserRequest) == "User Validation Failed"){
              complete(StatusCodes.BadRequest -> "Error! Please Enter proper email ID!")
            }
            else {
              complete(StatusCodes.BadRequest -> "Error! User already exists!")
            }
          }
        }
    }
}
