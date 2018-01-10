package model.body

import play.api.libs.json.Json

case class Body(body: Option[String]){
  def summary():Option[String] = ???
  def content: Content = ???
}

object Body{
////  implicit val bodyReads:Reads[Body] = new Reads[Body] {
////    override def reads(json: JsValue): JsResult[Body] = {
////      JsSuccess(Body((json \ "body").asOpt[String]))
////    }
//  }
  implicit val BodyJsonFormat = Json.format[Body]
}