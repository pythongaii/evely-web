package forms

import model.event._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

case class CreateEventData(body: String,
                           mail: Option[String],
                           noticeRange: Int,
                           openFlg: Boolean,
                           plans: List[Plan],
                           scope: String,
                           tel: Option[String],
                           title: String,
                           url: Option[String]
                          )

object CreateEventForm {

  val createEventForm = Form[CreateEventData](
    mapping(
      "body" -> nonEmptyText(maxLength = 1000),
      "mail" -> optional(text),
      "noticeRange" -> number,
      "openFlg" -> boolean,
      "schedules" -> list(mapping(
        "location" -> mapping(
          "name" -> nonEmptyText,
          "lat" -> of(doubleFormat),
          "lng" -> of(doubleFormat)
        )(Location.apply)(Location.unapply),
        "upcomingDate" -> mapping(
          "endDate" -> jodaDate("YYYY-MM-DD HH:mm:ss"),
          "startDate" -> jodaDate("YYYY-MM-DD HH:mm:ss")
        )(UpcomingDate.apply)(UpcomingDate.unapply)
      )(Plan.apply)(Plan.unapply)),
      "scope" -> text,
      "tel" -> optional(text),
      "title" -> nonEmptyText,
      "url" -> optional(text)
    )(CreateEventData.apply)(CreateEventData.unapply)
  )


}

