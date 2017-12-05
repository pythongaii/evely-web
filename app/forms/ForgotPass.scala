package forms

import play.api.data.Form
import play.api.data.Forms._

object ForgotPass {

  val form = Form(
    mapping(
      "password" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  val resetForm = Form(
    mapping(
      "password" -> nonEmptyText,
      "passwordConfirm" -> nonEmptyText
    )(Password.apply)(Password.unapply)
  )

  case class Password(password: String,
                      passwordConfirm: String)

  case class Data(password: String)
}
