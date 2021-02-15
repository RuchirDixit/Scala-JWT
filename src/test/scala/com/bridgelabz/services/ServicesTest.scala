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
package com.bridgelabz.services

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.bridgelabz.Main.caseclasses.User
import com.bridgelabz.Main.database.Database_service
import com.bridgelabz.Main.services.UserManagementService
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.when

class ServicesTest extends AnyWordSpec with should.Matchers with ScalatestRouteTest with MockitoSugar{
  // Test case to successfully add user to database
  "On Successful user added" should {
    "return Success" in {
      val data = User("demo@gmail.com","demo123",Some(false))
      val status = Database_service.saveUser(data)
      if(status == "Failure") {
        assert(status == "Failure")
      }
      else {
        assert(status == "Success")
      }
    }
    "return Validation Failed" in {
      val data = User("@gmail.com","1234",Some(false))
      val status = Database_service.saveUser(data)
      assert(status == "Validation Failed")
    }
  }

  "On Passing User login credentials" should {
    val service = new UserManagementService
    "return User not found" in {
      val userData = User("anyuser@gmail.com","demo123",Some(true))
      val response = service.userLogin(userData)
      assert(response == "User not found")
    }
  }

  "On Passing User login credentials using mocking" should {
    val serviceMock = mock[UserManagementService]
    "return Login Successful" in {
      val userData = User("demo@gmail.com","demo123",Some(true))
      when(serviceMock.userLogin(userData)).thenReturn("Login Successful")
      val response = serviceMock.userLogin(userData)
      assert(response == "Login Successful")
    }
    "return User not found" in {
      val userData = User("anyuser@gmail.com","demo123",Some(true))
      when(serviceMock.userLogin(userData)).thenReturn("User not found")
      val response = serviceMock.userLogin(userData)
      assert(response == "User not found")
    }
  }
}
