package com.bridgelabz.Main

import org.bson.BsonType
import org.mongodb.scala.Document
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Filters._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.matching.Regex
object Database_service {

  /**
   *
   * @param credentials : Data about the user that is getting stored in database
   * @return : If data is entered successfully then returns SUCCESS or else return FAILURE
   */
  def saveUser(credentials:User) = {
    val emailRegexPattern = "^[a-zA-Z0-9+-._]+@[a-zA-Z0-9.-]+$"
    if(credentials.email.matches(emailRegexPattern)){
      val userToBeAdded : Document = Document("email" -> credentials.email, "password" -> credentials.password, "isVerified" -> false)
      val ifUserExists = checkIfExists(credentials.email)
      if(ifUserExists == true)
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
   *
   * @param email : Email id of use to check whether this user already exists
   * @return : If user already exists it returns true or else it returns false
   */
  def checkIfExists(email : String): Boolean = {
    val data = Await.result(getUsers(),10.seconds)
    data.foreach(document => document.foreach(bsonObject =>
      if(bsonObject._2.getBsonType() == BsonType.STRING){
        if(bsonObject._2.asString().getValue().equals(email))
          return true
      }
    ))
    false
  }
  // Returns All the users present inside database in the form of future
  def getUsers() = {
    MongoDatabase.collection.find().toFuture()
  }

  // Returns user that matches with given email ID and excluded ObjectId while returning
  def getUsersUsingFilter(email : String) = {
      println("MongoDatabase:"+MongoDatabase.collection.find())
      MongoDatabase.collection.find(equal("email",email)).projection(excludeId()).toFuture()
  }

}
