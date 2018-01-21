package model.event

import java.util.Date

import model.body.Body
import model.user.RegisteredUser
import play.api.libs.json.Reads._
import play.api.libs.json.{Reads, _}


case class Event(id: String,
                 title: String,
                 host: RegisteredUser,
                 body: Body,
                 updateDate: Option[String],
                 url: Option[String],
                 tel: Option[String],
                 mail: Option[String],
                 topImage: Option[String],
                 createdAt: Option[Date],
                 noticeRange: Option[Int],
                 openFlg: Option[Boolean],
                 schedules: List[Plan],
                 scope: Option[String],
                 isReviewed: Option[Boolean]
                )

object Event {
  implicit val eventReader:Reads[Event] = new Reads[Event] {
    override def reads(json: JsValue): JsResult[Event] = {
      val j = json
      JsSuccess(
        Event(
          (json \ "id").as[String],
          (json \ "title").as[String],
          RegisteredUser((json \ "host" \ "id").as[String],Option.empty,(json \ "host" \ "name").as[String],Option.empty, Option.empty),
          Body((json \ "body").asOpt[String]),
          (json \ "updateDate").asOpt[String],
          (json \ "url").asOpt[String],
          (json \ "tel").asOpt[String],
          (json \ "mail").asOpt[String],
          (json \ "topImage").asOpt[String],
          (json \ "createdAt").asOpt[Date],
          (json \ "noticeRange").asOpt[Int],
        (json \ "openFlg").asOpt[Boolean],
        (json \ "schedules").as[List[Plan]],
          (json \ "scope").asOpt[String],
          (json \ "isReviewed").asOpt[Boolean]
      ))
    }
  }

//  implicit val EventJsonFormat = Json.format[Event]
}
