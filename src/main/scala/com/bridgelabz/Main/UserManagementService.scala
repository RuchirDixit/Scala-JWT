package com.bridgelabz.Main

class UserManagementService {

  def userLogin(loginRequest: Request): String = {
    "Login Successful"
  }

  def createUser(createUserRequest: Request):  String = "User Created"

  def protectedContent: String  = "This method is secured"

}