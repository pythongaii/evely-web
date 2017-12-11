package model.user

import java.util.Optional

//import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import model.formaction.MailAddress
import play.api.libs.json.Json


case class RegisterdUser(userName: String, mailAddress: MailAddress,
                           realName: String, tel: Option[String]
//                           loginInfo: LoginInfo
                          )

object RegisterdUser {
  implicit val RegisterdUserJsonFormat = Json.format[RegisterdUser]
}