package model.body

import play.api.libs.json.Json

case class Body(summary: Option[String], content: Content)

object Body{
  implicit val BodyJsonFormat = Json.format[Body]
}