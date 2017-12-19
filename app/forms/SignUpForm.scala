package forms

import model.user.{RegisteredUser, RegistringUser}
import play.api.data.Form
import play.api.data.Forms._


object SignUpForm {

  val registringDataForm = Form[RegistringData](
    mapping(
      "mail" -> nonEmptyText
    )(RegistringData.apply)(RegistringData.unapply)
  )

  case class RegistringData(mailaddress: String)

  val registeredDataForm = Form[RegisteredData](
    mapping(
      "id" -> nonEmptyText(minLength = 4, maxLength = 15),
      "mail" -> nonEmptyText,
      "name" -> nonEmptyText(minLength = 1, maxLength = 50),
      "password" -> tuple(
        "password1" -> nonEmptyText(minLength = 8),
        "password2" -> nonEmptyText(minLength = 8)
      ).verifying("password",passwords => passwords._1 == passwords._2),
      "tel" -> nonEmptyText
    )(RegisteredData.apply)(RegisteredData.unapply)
  )

  case class RegisteredData(id: String,
                           mail: String,
                           name: String,
                           password: (String, String),
                           tel: String = "")
}