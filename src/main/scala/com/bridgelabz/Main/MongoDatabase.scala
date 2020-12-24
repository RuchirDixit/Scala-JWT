package com.bridgelabz.Main
import akka.actor.ActorSystem
import org.mongodb.scala.{Document,MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.ExecutionContext

object MongoDatabase {
  implicit val system = ActorSystem("Scala_jwt-App")
  implicit val executor: ExecutionContext = system.dispatcher
  val mongoClient: MongoClient = MongoClient()
  val databaseName = sys.env("database_name")
  // Getting mongodb database
  val database: MongoDatabase = mongoClient.getDatabase(databaseName)
  val collectionName = sys.env("collection_name")
  // Getting mongodb collection
  val collection: MongoCollection[Document] = database.getCollection(collectionName)
  collection.drop()
}
