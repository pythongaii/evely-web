package model.event

import play.api.libs.json.Json

case class APIEvent(body: String,
                    mail: Option[String],
                    place: APIPlace,
                    tel: Option[String],
                    title: String,
                    upcomingDate: UpcomingDate,
                    url: Option[String])

object APIEvent {
  implicit val APIEventFormat = Json.format[APIEvent]
}