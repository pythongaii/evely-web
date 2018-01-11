package model.event

import play.api.libs.json.Json

case class Plan(location: Location, upcomingDate: UpcomingDate)

object Plan{
  implicit val format = Json.format[Plan]
}
