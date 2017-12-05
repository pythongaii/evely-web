package forms

import model.user.{RegisterdUser, RegistringUser}
import play.api.data.Form
import play.api.data.Forms._


object SignUpForm {

  val registringDataForm = Form[RegistringData](
    mapping(
      "mailaddress" -> nonEmptyText
    )(RegistringData.apply)(RegistringData.unapply)
  )

  case class RegistringData(mailaddress: String)

  val registerdDataForm = Form[RegisterdData](
    mapping(
      "userName" -> nonEmptyText,
      "realName" -> nonEmptyText,
      "password" -> nonEmptyText,
      "mailaddress" -> nonEmptyText,
      "tel" -> optional(text)
    )(RegisterdData.apply)(RegisterdData.unapply)
  )

  case class RegisterdData(userName: String,
                           realName: String,
                           password: String,
                           mailaddress: String,
                           tel: Option[String])
}