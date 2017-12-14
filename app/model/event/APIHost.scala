package model.event

import play.api.libs.json.Json

case class APIHost(id:String, name:String) {

}

object APIHost{
  implicit val APIHostFormat = Json.format[APIHost]
}
