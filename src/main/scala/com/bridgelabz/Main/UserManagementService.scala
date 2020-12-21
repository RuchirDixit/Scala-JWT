package com.bridgelabz.Main

class UserManagementService {

  /**
   *
   * @param loginRequest : Request object containing details of login
   * @return : If successfull login return "Login Successful"
   */
  def userLogin(loginRequest: Request): String = {
    "Login Successful"
  }

  /**
   *
   * @param createUserRequest : Request object containing details of registering user
   * @return : If successfull login return "User created"
   */
  def createUser(createUserRequest: Request):  String = "User Created"

  def protectedContent: String  = "This method is secured"

}