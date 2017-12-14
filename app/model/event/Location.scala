package model.event

import play.api.libs.json.Json

case class Location(name: String, address: String, latlng: String)

object Location{
  implicit val locationFormat = Json.format[Location]
}