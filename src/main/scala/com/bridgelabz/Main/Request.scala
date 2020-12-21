package com.bridgelabz.Main
import play.api.libs.json.{Json, Reads}
// Request case class having fields email and password
case class Request(
                         email: String,
                         password: String
                       )

object Request {
  implicit val requestReads: Reads[Request] = Json.reads[Request]
}