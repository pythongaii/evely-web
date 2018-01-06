package forms

import play.api.data.Form
import play.api.data.Forms._

object SearchEventForm {
  val form = Form[Keyword](
    mapping("keyword" -> text)(Keyword.apply)(Keyword.unapply)
  )
}

case class Keyword(keyword: String)
