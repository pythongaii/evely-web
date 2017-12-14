package model.event

import model.user.RegisteredUser
import play.api.libs.json.Json

case class UpcomingDate(endDate: String, startDate: String)

//case class UpcomingDate(year: Int, month: Int, day: Int, period: String,
//                        usersJoining: Option[List[RegisteredUser]]
//                         )

object UpcomingDate{
  implicit val UpcomingDateJsonFormat = Json.format[UpcomingDate]
}