package com.bridgelabz
// TODO : 1. Check using MongoDatabase.find whether you are able to access data from database;
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.routing.Router
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import com.bridgelabz.Main.{Database_service, MongoDatabase, User, UserManagementRoutes, UserManagementService}
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec
import concurrent.duration._
import scala.concurrent.Await
class RegistrationTest extends AnyWordSpec with should.Matchers with ScalatestRouteTest {

  "A Router" should {
    "login successfully" in {
      val jsonRequest = ByteString(
        s"""
           |{
           |    "email":"ruchirtd96@gmail.com",
           |    "password":"demo"
           |}
              """.stripMargin)
      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/user/register",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))
//      val users = Await.result(MongoDatabase.collection.find().toFuture(),120.seconds)
//      users.foreach(doc => doc.foreach(value => println("Value::"+value)))
//      val userManagementService = new UserManagementService
//      postRequest ~>  Route.seal(new UserManagementRoutes(userManagementService).routes) ~> check {
//        status.isSuccess() shouldEqual true
//        //responseAs[String] shouldEqual "Successfully logged in!"
//      }
    }
  }
}
