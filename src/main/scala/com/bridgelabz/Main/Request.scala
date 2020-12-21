package com.bridgelabz.Main
import play.api.libs.json.{Json, Reads}

case class Request(
                         email: String,
                         password: String
                       )

object Request {
  implicit val requestReads: Reads[Request] = Json.reads[Request]
}