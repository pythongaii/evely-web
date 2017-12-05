package model.event

import java.awt.Image
import java.util.Date

import model._
import model.body.Body
import model.formaction.{FormAction, VerificationNeeded}
import model.user.RegisterdUser
import play.api.libs.json.Json


case class Event(eventID: Int,
                   title: String,
                   holder: RegisterdUser,
                   body: Body,
                   place: Location,
                   updateDate: Date,
                   upcomingDates: List[UpcomingDate],
                   url: Option[FormAction],
                   tel: Option[VerificationNeeded],
                   mailAddress: Option[VerificationNeeded],
                   topImage: Option[Image]
                  )

object Event {
  // 一時コメントアウト
//  implicit val EventJsonFormat = Json.format[Event]
}
