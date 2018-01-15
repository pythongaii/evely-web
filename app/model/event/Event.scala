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
                 noticeRange: Int,
                 openFlg: Boolean,
                 plans: List[Plan],
                 scope: String
                )

object Event {
  implicit val eventReader:Reads[Event] = new Reads[Event] {
    override def reads(json: JsValue): JsResult[Event] = {
      JsSuccess(
        Event(
          (json \ "id").validate[String].get,
          (json \ "title").as[String],
          RegisteredUser((json \ "host" \ "id").as[String],Option.empty,(json \ "host" \ "name").as[String],Option.empty, Option.empty),
          Body((json \ "body").asOpt[String]),
          (json \ "updateDate").asOpt[String],
          (json \ "url").asOpt[String],
          (json \ "tel").asOpt[String],
          (json \ "mail").asOpt[String],
          (json \ "topImage").asOpt[String],
          (json \ "createdAt").asOpt[Date],
          (json \ "noticeRange").as[Int],
        (json \ "openFlg").as[Boolean],
        (json \ "plans").as[List[Plan]],
          (json \ "scope").as[String]
      ))
    }
  }

//  implicit val EventJsonFormat = Json.format[Event]
}
