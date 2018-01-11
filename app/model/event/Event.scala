package model.event

import model.body.Body
import model.user.RegisteredUser
import play.api.libs.json.Reads._
import play.api.libs.json.{Reads, _}


case class Event(id: String,
                 title: String,
                 host: RegisteredUser,
                 body: Body,
                 place: Location,
                 updateDate: Option[String],
                 upcomingDate: List[UpcomingDate],
                 url: Option[String],
                 tel: Option[String],
                 mail: Option[String],
                 topImage: Option[String],

                )

object Event {
  implicit val eventReader:Reads[Event] = new Reads[Event] {
    override def reads(json: JsValue): JsResult[Event] = {
      JsSuccess(
        Event(
          (json \ "id").as[String],
          (json \ "title").as[String],
          RegisteredUser((json \ "host" \ "id").as[String],Option.empty,(json \ "host" \ "name").as[String],Option.empty, Option.empty),
          Body((json \ "body").asOpt[String]),
          Location((json \ "place"\ "name").as[String],"",(json \ "place"\ "lat").as[Double].toString(), (json \ "place"\ "lng").as[Double].toString()),
          (json \ "updateDate").asOpt[String],
          List[UpcomingDate](UpcomingDate((json \ "upcomingDate"\ "endDate").asOpt[String],(json \ "upcomingDate"\ "endDate").asOpt[String])),
          (json \ "url").asOpt[String],
          (json \ "tel").asOpt[String],
          (json \ "mail").asOpt[String],
          (json \ "topImage").asOpt[String]
      ))
    }
  }

//  implicit val EventJsonFormat = Json.format[Event]
}
