package model.user

import java.util.Optional

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

//import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import model.formaction.MailAddress
import play.api.libs.json.Json


case class RegisteredUser(id: String, mail: MailAddress,
                          name: String, password: String, tel: String
                          )

object RegisteredUser {
  implicit val RegisterdUserJsonFormat = Json.format[RegisteredUser]
}