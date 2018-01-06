package model.event

import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Location(name: String, address: String = "", lat:String, lng: String)

object Location{
  implicit val locationReads:Reads[Location] = new Reads[Location] {
    override def reads(json: JsValue): JsResult[Location] = {
      JsSuccess(
        Location(
          (json \ "name").as[String],
          "",
          (json \ "lat").as[String],
          (json \ "lng").as[String]
        )
      )
    }
  }
  implicit val locationFormat = Json.format[Location]
}