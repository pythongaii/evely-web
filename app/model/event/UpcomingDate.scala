package model.event

import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, Reads, _}

case class UpcomingDate(endDate: DateTime, startDate: DateTime) {
  val startMonthAndDay = startDate.toString("MM月dd日")
}

object UpcomingDate{
  val tz = java.util.TimeZone.getTimeZone("UTC")
  val df = ISODateTimeFormat.dateTimeNoMillis().withZone(DateTimeZone.getDefault)
    implicit val upcomingDateReads:Reads[UpcomingDate] = new Reads[UpcomingDate] {
      override def reads(json: JsValue): JsResult[UpcomingDate] = {
        JsSuccess(
          UpcomingDate(
            df.parseDateTime((json \ "endDate").as[String]),
            df.parseDateTime((json \ "startDate").as[String])
          )
        )
      }
    }

    implicit val upcomingDateWrites:Writes[UpcomingDate] = new Writes[UpcomingDate] {
      override def writes(o: UpcomingDate): JsValue = {
        Json.obj(
          "endDate" -> df.print(o.endDate),
          "startDate" ->  df.print(o.startDate)
        )
      }
    }
}