package forms

import play.api.data.Form
import play.api.data.Forms._

object SearchEventForm {
  val form = Form[Keyword](
    mapping(
      "keyword" -> text,
      "limit" -> text,
      "offset" -> text
    )(Keyword.apply)(Keyword.unapply)
  )
}

case class Keyword(keyword: String, limit: String, offset:String)
