package model.user

import play.api.libs.json.Json

case class RegistringUser(mail: String)

object RegistringUser {
  implicit val RegistringUserJsonFormat = Json.format[RegistringUser]
}