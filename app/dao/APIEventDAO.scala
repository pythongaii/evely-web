package dao

import javax.inject.Inject

import model.event.APIEvent
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.{Cookie, RequestHeader}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class APIEventDAO @Inject()(ws: WSClient) extends PlainDAO[String, APIEvent, WSResponse] {
  val COOKIE_NAME = "evely_auth"


  override def add(obj: APIEvent): Future[WSResponse] = ???

  override def find(key: String): Future[Option[WSResponse]] = ???

  override def remove(key: String): Future[Unit] = ???

  override def save(obj: APIEvent, request: RequestHeader): Future[WSResponse] = {
    val optionCookie: Option[Cookie] = request.cookies.get(COOKIE_NAME)
    optionCookie match {
      case Some(cookie) => {
        val tokenString = cookie.value
        val jso = Json.obj(
          "body" -> obj.body,
          "mail" -> obj.mail,
          "place" ->
            Json.obj(
              "lat" -> obj.place.lat,
              "lng" -> obj.place.lng,
              "name" -> obj.place.name
            ),
          "tel" -> obj.tel,
          "title" -> obj.title,
          "upcomingDate" ->
            Json.obj(
              "endDate" -> obj.upcomingDate.endDate.concat("Z"),
              "startDate" -> obj.upcomingDate.startDate.concat("Z")
            ),
          "url" -> obj.url
        )

        val response = ws.url("http://160.16.140.145:8888/api/develop/v1/events").
          withHeaders(("Content-Type" -> "application/json"), ("Authorization", "Bearer " + tokenString)).withMethod("POST").withBody(jso).execute()
        response.flatMap {
          case res => {
            Future.successful(res)
          }
        }
      }
    }
  }

  override def update(obj: APIEvent): Future[WSResponse] = ???
}
