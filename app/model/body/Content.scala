package model.body

import play.api.libs.json.Json


/**
  * 未定義
  * いったんcase class
  */
case class Content(dummy: String)


object Content{
  implicit val format = Json.format[Content]
}