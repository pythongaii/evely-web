package model.event

import play.api.libs.json.Json

case class APIPlace(lat: Option[Double], lng: Option[Double], name: String)

object APIPlace{
  implicit val APIPlaceFormat = Json.format[APIPlace]
}
