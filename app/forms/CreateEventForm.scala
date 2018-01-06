package forms

import model.event.{APIEvent, APIPlace, UpcomingDate}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json.Json

case class CreateEventData(body: String,
                    mail: Option[String],
                    place: APIPlace,
                    tel: Option[String],
                    title: String,
                    upcomingDate: UpcomingDate,
                    url: Option[String])

object CreateEventForm {

  val createEventForm = Form[CreateEventData](
    mapping(
      "body" -> nonEmptyText(maxLength = 1000),
      "mail" -> optional(text),
      "place" -> mapping(
        "lat" -> optional(of(doubleFormat)),
        "lng" -> optional(of(doubleFormat)),
        "name" -> nonEmptyText
      )(APIPlace.apply)(APIPlace.unapply),
      "tel" -> optional(text),
      "title" -> nonEmptyText,
      "upcomingDate" -> mapping(
        "endDate" -> nonEmptyText,
        "startDate" -> nonEmptyText
      )(UpcomingDate.apply)(UpcomingDate.unapply),
      "url" -> optional(text)
    )(CreateEventData.apply)(CreateEventData.unapply)
  )


}

