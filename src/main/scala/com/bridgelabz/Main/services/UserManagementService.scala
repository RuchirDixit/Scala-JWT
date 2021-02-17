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
package com.bridgelabz.Main.services

import com.bridgelabz.Main.caseclasses.User
import com.bridgelabz.Main.database.DatabaseService
import com.typesafe.scalalogging.LazyLogging
import org.bson.BsonType

import scala.concurrent.Await
import scala.concurrent.duration._
class UserManagementService extends LazyLogging {
  /**
   * To login using credentials and return login successful if right details are passed
   * @param loginRequest : Request object containing details of login
   * @return : If successful login return "Login Successful"
   */
  def userLogin(loginRequest: User): String = {
    val users = Await.result(DatabaseService.getUsersUsingFilter(loginRequest.email),60.seconds)
    users.foreach(document => document.foreach(bsonObject =>
        if(bsonObject._2.asString().getValue.equals(loginRequest.email)){
          logger.info("Inside if value (email):" + bsonObject._2.asString().getValue)
          users.foreach(document => document.foreach(bsonStringObject =>
            if(bsonStringObject._2.asString().getValue.equals(loginRequest.password)){
              logger.info("Inside if value (password):" + bsonStringObject._2.asString().getValue)
              users.foreach(document => document.foreach(bsonObject =>
                if(bsonObject._2.getBsonType() == BsonType.BOOLEAN){
                  if(bsonObject._2.asBoolean().getValue == false){
                    return "User Not verified"
                  }
                }
              ))
              return "Login Successful"
            }
          ))
        }
    ))
    "User not found"
  }

  /**
   * To pass user credentials and create user and add to database
   * @param createUserRequest : Request object containing details of registering user
   * @return : If successful login return "User created"
   */
  def createUser(createUserRequest: User):  String = {
    val status = DatabaseService.saveUser(createUserRequest)
    if (status.equals("Success")){
      "User created"
    }
      else if(status.equals("Validation Failed")){
      "User Validation Failed"
    }
    else {
      "User not created"
    }
  }
}
