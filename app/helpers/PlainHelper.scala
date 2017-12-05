package helpers

import views.html.helper.FieldConstructor

object PlainHelper {
  implicit val myFields = FieldConstructor(views.html.plain_field_constructor.render)
}
