package com.bridgelabz.Main

import org.bson.BsonType
import org.mongodb.scala.Document
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Filters._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
object Database_service {

  /**
   *
   * @param credentials : Data about the user that is getting stored in database
   * @return : If data is entered successfully then returns SUCCESS or else return FAILURE
   */
  def saveUser(credentials:Request) = {
    val userToBeAdded : Document = Document("email" -> credentials.email, "password" -> credentials.password)
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

  def getUsersUsingFilter(email : String) = {
    MongoDatabase.collection.find(equal("email",email)).projection(excludeId()).toFuture()
  }

}
