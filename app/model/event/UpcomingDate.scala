package model.event

import play.api.libs.json.Json

case class UpcomingDate(endDate: Option[String] = Option.empty, startDate: Option[String] = Option.empty)

//case class UpcomingDate(year: Int, month: Int, day: Int, period: String,
//                        usersJoining: Option[List[RegisteredUser]]
//                         )

object UpcomingDate{
  implicit val UpcomingDateJsonFormat = Json.format[UpcomingDate]
}