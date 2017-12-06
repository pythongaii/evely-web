package forms

import play.api.data.Form
import play.api.data.Forms._

object ForgotPass {

  val form = Form(
    mapping(
      "password" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  val resetForm = Form[Password](
    mapping(
      "password" -> tuple(
        "password1" -> nonEmptyText,
        "password2" -> nonEmptyText
      ).verifying(passwords => passwords._1 == passwords._2),
    )(Password.apply)(Password.unapply)
  )

  case class Password(password: (String, String))

  case class Data(password: String)
}
