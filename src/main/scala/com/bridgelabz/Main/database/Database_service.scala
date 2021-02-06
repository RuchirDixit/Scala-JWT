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
package com.bridgelabz.Main.database

import com.bridgelabz.Main.caseclasses.User
import com.typesafe.scalalogging.LazyLogging
import org.bson.BsonType
import org.mongodb.scala.Document
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import scala.concurrent.{Await, Future}

object Database_service extends LazyLogging{

  /**
   * method to save user to database and if user already exists return Failure
   * @param credentials : Data about the user that is getting stored in database
   * @return : If data is entered successfully then returns SUCCESS or else return FAILURE
   */
  def saveUser(credentials:User) : String = {
    val emailRegexPattern = "^[a-zA-Z0-9+-._]+@[a-zA-Z0-9.-]+$"
    if(credentials.email.matches(emailRegexPattern)){
      val userToBeAdded : Document = Document("email" -> credentials.email, "password" -> credentials.password, "isVerified" -> false)
      val ifUserExists = checkIfExists(credentials.email)
      if(ifUserExists)
      {
        "Failure"
      }
      else
      {
        val future = MongoDatabase.collection.insertOne(userToBeAdded).toFuture()
        Await.result(future,10.seconds)
        "Success"
      }
    }
    else {
      "Validation Failed"
    }
  }

  /**
   * To check if user already exists or not
   * @param email : Email id of use to check whether this user already exists
   * @return : If user already exists it returns true or else it returns false
   */
  def checkIfExists(email : String): Boolean = {
    val users = Await.result(getUsers(),10.seconds)
    users.foreach(document => document.foreach(bsonObject =>
      if(bsonObject._2.getBsonType() == BsonType.STRING){
        if(bsonObject._2.asString().getValue().equals(email)) {
          return true
        }
      }
    ))
    false
  }

  /**
   * method to get all the users from database
   * @return : Future[Seq[Document]]
   */
  def getUsers(): Future[Seq[Document]] = {
    MongoDatabase.collection.find().toFuture()
  }

  /**
   * method to get user based on email id of user
   * @param email : Email id of user to check if it the user already exists in the system or no
   * @return : Future of data about user by excluding ObjectId for better understanding of data
   */
  def getUsersUsingFilter(email : String) : Future[Seq[Document]] = {
      logger.info("MongoDatabase:" + MongoDatabase.collection.find())
      MongoDatabase.collection.find(equal("email",email)).projection(excludeId()).toFuture()
  }

}
