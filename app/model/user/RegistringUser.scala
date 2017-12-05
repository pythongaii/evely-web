package model.user

import model.formaction.MailAddress
import play.api.libs.json.Json


case class RegistringUser(mailAddress: String)

object RegistringUser {
  implicit val RegistringUserJsonFormat = Json.format[RegistringUser]
}