package com.bridgelabz.Main

class UserManagementService {

  /**
   *
   * @param loginRequest : Request object containing details of login
   * @return : If successful login return "Login Successful"
   */
  def userLogin(loginRequest: Request): String = {
    "Login Successful"
  }

  /**
   *
   * @param createUserRequest : Request object containing details of registering user
   * @return : If successfull login return "User created"
   */
  def createUser(createUserRequest: Request):  String = {
    val status = Database_service.saveUser(createUserRequest)
    if (status.equals("Success")){
      "User created"
    }
    else {
      "User not created"
    }
  }

  def protectedContent: String  = "This method is secured"

}