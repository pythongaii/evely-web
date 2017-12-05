package model.event

import model.user.RegisterdUser
import play.api.libs.json.Json


case class UpcomingDate(year: Int, month: Int, day: Int, period: String,
                        usersJoining: Option[List[RegisterdUser]]
                         )

object UpcomingDate{
  implicit val UpcomingDateJsonFormat = Json.format[UpcomingDate]
}