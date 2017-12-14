package model.event

import play.api.libs.json.Json

case class APIResponseEvent(body: String,
                            host: APIHost,
                            id: String,
                            mail: String,
                            place: APIPlace,
                            tel: String,
                            title: String,
                            upcomingDate: UpcomingDate,
                            updateDate: String, url: String) {

}

object APIResponseEvent{
  implicit val APIResponseEventFormat = Json.format[APIResponseEvent]
}