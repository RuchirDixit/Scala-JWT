package com.bridgelabz.Main

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration._
class UserManagementService extends LazyLogging{

  /**
   *
   * @param loginRequest : Request object containing details of login
   * @return : If successful login return "Login Successful"
   */
  def userLogin(loginRequest: Request): String = {
    val users = Await.result(Database_service.getUsersUsingFilter(loginRequest.email),60.seconds)
    users.foreach(document => document.foreach(bsonStringObject =>
    if(bsonStringObject._2.asString().getValue.equals(loginRequest.email)){
      logger.info("Inside if value (email):" + bsonStringObject._2.asString().getValue)
      users.foreach(document => document.foreach(bsonStringObject =>
        if(bsonStringObject._2.asString().getValue.equals(loginRequest.password)){
          logger.info("Inside if value (password):" + bsonStringObject._2.asString().getValue)
          return "Login Successful"
        }))
    }
    ))
    "User not found"
  }

  /**
   *
   * @param createUserRequest : Request object containing details of registering user
   * @return : If successfull login return "User created"
   */
  def createUser(createUserRequest: Request):  String = {
    val status = Database_service.saveUser(createUserRequest)
    if (status.equals("Success")){
      "User created"
    }
    else {
      "User not created"
    }
  }

  def protectedContent: String  = "This method is secured"

}