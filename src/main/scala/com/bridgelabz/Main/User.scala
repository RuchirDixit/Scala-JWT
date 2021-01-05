package com.bridgelabz.Main
import play.api.libs.json.{Json, Reads}
// Request case class having fields email and password
case class User(
                         email: String,
                         password: String,
                         isVerified: Option[Boolean] = None
                       )

object User {
  implicit val requestReads: Reads[User] = Json.reads[User]
}