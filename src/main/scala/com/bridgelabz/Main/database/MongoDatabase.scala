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

import com.bridgelabz.Main.actorsystem.ActorSystemFactory
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import scala.concurrent.ExecutionContext

object MongoDatabase {
  val system = ActorSystemFactory.system
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
