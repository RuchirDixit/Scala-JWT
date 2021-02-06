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
package com.bridgelabz.Main.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.bridgelabz.Main.caseclasses.User
import com.bridgelabz.Main.database.MongoDatabase
import com.bridgelabz.Main.services.UserManagementService
import com.bridgelabz.Main.jwt.TokenAuthorization
import com.nimbusds.jose.JWSObject
import com.typesafe.scalalogging.LazyLogging
import courier.Defaults._
import courier._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import javax.mail.internet.InternetAddress
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.util.{Failure, Success}
class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport with LazyLogging {
  val routes: Route =
    pathPrefix("user") {
      /**
       * for login using post request and Status code as OK
       * @input : It accepts email and password as body
       * @Return :  success on successful login or else returns unauthorized
       */
      path("login") {
        (post & entity(as[User])) { loginRequest =>
          logger.info("Login response: " + service.userLogin(loginRequest))
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
        /**
         * It fetches user by decoding the token and updates the isVerified field to true
         * @input : It accepts parameters token and name from the link
         * @return :  success on successful verification of user and error if verification failed
         */
      path("verify"){
        get {
          parameters('token.as[String],'email.as[String]) {
            (token,email) =>
              val jwsObject = JWSObject.parse(token)
              val updateUserAsVerified = MongoDatabase.collection.updateOne(equal("email",email),set("isVerified",true)).toFuture()
              if(jwsObject.getPayload.toJSONObject.get("email").equals(email)){
                onComplete(updateUserAsVerified) {
                  case Success(_) =>
                    logger.info("Successfully verified user!")
                    complete("User successfully verified and registered!")
                }
              }
              else {
                complete("User could not be verified!")
              }
          }
        }
      } ~
        /**
         * It generates jwt token on successful registration of user and sends email to user for verification
         * @input : It accepts email,password
         * @return : success on successful registration of user or else error message if user already exists
         */
        path("register") {
          (post & entity(as[User])) { createUserRequest =>
            if (service.createUser(createUserRequest) == "User created") {
              val token: String = TokenAuthorization.generateToken(createUserRequest.email)
              val mailer = Mailer("smtp.gmail.com", sys.env("smtp_port").toInt)
                .auth(true)
                .as(sys.env("sender_email"),sys.env("sender_password"))
                .startTls(true)()
              mailer(Envelope.from(new InternetAddress(sys.env("sender_email")))
                .to(new InternetAddress(createUserRequest.email))
                .subject("Token")
                .content(Text("Thanks for registering! Click on this link to verify your email address: http://" +
                  sys.env("Host") + ":" + sys.env("Port_number") + "/user/verify?token="
                  + token + "&email=" + createUserRequest.email)))
                .onComplete {
                case Success(_) => logger.info("Message delivered. Email verified!")
                case Failure(error) => logger.error(error.toString)
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
