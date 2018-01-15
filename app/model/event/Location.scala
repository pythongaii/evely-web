package model.event

import play.api.libs.json._


case class Location(name: String, lat:Double, lng: Double)

object Location{
//  implicit val locationReads:Reads[Location] = new Reads[Location] {
//    override def reads(json: JsValue): JsResult[Location] = {
//      JsSuccess(
//        Location(
//          (json \ "name").as[String],
//          (json \ "lat").as[Double],
//          (json \ "lng").as[Double]
//        )
//      )
//    }
//  }
//  implicit val locationwrite:Writes[Location] = new Writes[Location] {
//    override def writes(o: Location): JsValue = ???
//  }
  implicit val locationformat = Json.format[Location]
}