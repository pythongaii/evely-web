package model.event

import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, Reads, _}

case class UpcomingDate(endDate: Option[DateTime] = Option.empty, startDate: Option[DateTime] = Option.empty)

object UpcomingDate{
  val tz = java.util.TimeZone.getTimeZone("UTC")
  val df = ISODateTimeFormat.dateTime().withZone(DateTimeZone.getDefault)
    implicit val upcomingDateReads:Reads[UpcomingDate] = new Reads[UpcomingDate] {
      override def reads(json: JsValue): JsResult[UpcomingDate] = {
        JsSuccess(
          UpcomingDate(
            Option(df.parseDateTime((json \ "endDate").asOpt[String].get)),
            Option(df.parseDateTime((json \ "startDate").asOpt[String].get))
          )
        )
      }
    }

    implicit val upcomingDateWrites:Writes[UpcomingDate] = new Writes[UpcomingDate] {
      override def writes(o: UpcomingDate): JsValue = {
        Json.obj(
          "endDate" -> df.print(o.endDate.get),
          "startDate" ->  df.print(o.startDate.get)
        )
      }
    }
}