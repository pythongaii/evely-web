package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

object SignInForm {

  val signInForm = Form[SignInData](
    mapping(
      "id" -> nonEmptyText,
      "password" -> nonEmptyText
    )(SignInData.apply)(SignInData.unapply)
  )

}

case class SignInData(id: String,
                      password: String)

