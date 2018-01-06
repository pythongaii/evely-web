package model.body

import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Body(body: String){
  def summary():Option[String] = ???
  def content: Content = ???
}

object Body{
  implicit val bodyReads:Reads[Body] = new Reads[Body] {
    override def reads(json: JsValue): JsResult[Body] = {
      JsSuccess(Body((json \ "body").as[String]))
    }
  }
  implicit val BodyJsonFormat = Json.format[Body]
}