package model.event

import java.awt.Image
import java.util.Date

import play.api.libs.functional.syntax._
import model._
import model.body.Body
import model.formaction.{FormAction, MailAddress, VerificationNeeded}
import model.user.RegisteredUser
import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.json.Reads._
import play.api.libs.json._


case class Event(id: String,
                 title: String,
                 host: RegisteredUser,
                 body: Body,
                 place: Location,
                 updateDate: String,
                 upcomingDate: List[UpcomingDate],
                 url: Option[String],
                 tel: Option[String],
                 mail: Option[String],
                 topImage: Option[String]
                )

object Event {
  implicit val eventReader:Reads[Event] = new Reads[Event] {
    override def reads(json: JsValue): JsResult[Event] = {
      json
      JsSuccess(
        Event(
          "",
          (json \ "title").as[String],
          RegisteredUser((json \ "host" \ "id").as[String],Option.empty,(json \ "host" \ "name").as[String],Option.empty, Option.empty),
          Body(""),
          Location((json \ "place"\ "name").as[String],"",(json \ "place"\ "lat").as[Double].toString(), (json \ "place"\ "lng").as[Double].toString()),
          "",
          List[UpcomingDate](UpcomingDate((json \ "upcomingDate"\ "endDate").as[String],(json \ "upcomingDate"\ "endDate").as[String])),
          Option(""),
          Option(""),
          Option(""),
          Option.empty
      ))
    }
  }

//  implicit val EventJsonFormat = Json.format[Event]
}
