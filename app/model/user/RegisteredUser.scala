package model.user

import java.util.Optional

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

//import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import model.formaction.MailAddress
import play.api.libs.json.Json


case class RegisteredUser(userName: String, mailAddress: MailAddress,
                          realName: String, tel: Option[String]
                          )

object RegisteredUser {
  implicit val RegisterdUserJsonFormat = Json.format[RegisteredUser]
}