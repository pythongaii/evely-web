package model

import play.api.libs.json.Json

case class Token(token: String) {
  val cookieString = token.substring(token.indexOf(" ") + 1)
}

object Token {
  implicit val TokenFormat = Json.format[Token]
}
