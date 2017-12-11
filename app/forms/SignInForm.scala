package forms

import play.api.data.Form
import play.api.data.Forms._

object SignInForm {

  val signInForm = Form[SignInData](
    mapping(
      "userID" -> nonEmptyText,
      "password" -> nonEmptyText
    )(SignInData.apply)(SignInData.unapply)
  )

  case class SignInData(userName:String,
                        password:String)

}

