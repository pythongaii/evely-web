package model.user

import model.formaction.MailAddress
import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class RegisteredUser(id: String, mail: Option[MailAddress],
                          name: String, password: Option[String],
                          tel: Option[String])

object RegisteredUser {
  implicit val registeredUserReads:Reads[RegisteredUser] = new Reads[RegisteredUser]{

    override def reads(json: JsValue): JsResult[RegisteredUser] = {
      val id = (json \ "id").as[String]
      val name = (json \ "name").as[String]
      JsSuccess(RegisteredUser(id,Option.empty[MailAddress],name,Option.empty,Option.empty))
    }
  }
  implicit val RegisterdUserJsonFormat = Json.format[RegisteredUser]
}