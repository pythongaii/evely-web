package forms

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
      "password" -> tuple(
        "password1" -> nonEmptyText(minLength = 6),
        "password2" -> nonEmptyText(minLength = 6)
      ).verifying(passwords => passwords._1 == passwords._2),
      "mailaddress" -> nonEmptyText,
      "tel" -> optional(text)
    )(RegisterdData.apply)(RegisterdData.unapply)
  )

  case class RegisterdData(userName: String,
                           realName: String,
                           password: (String, String),
                           mailaddress: String,
                           tel: Option[String])
}