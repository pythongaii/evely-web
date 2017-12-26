package dao

import javax.inject.Inject

import model.event.APIEvent
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.RequestHeader
import utils.ConfigProvider._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class APIEventDAO @Inject()(ws: WSClient) extends PlainDAO[APIEvent, WSResponse] {

  override def find(key: String): Future[Option[WSResponse]] = ???

  override def remove(key: String, request: RequestHeader): Future[Unit] = ???

  override def save(obj: APIEvent, request: RequestHeader): Future[WSResponse] = {

    val optionalCookie = request.cookies.get(COOKIE_NAME)

    optionalCookie match {
      case Some(cookie) => {
        val tokenString = cookie.value
        val jsObject = Json.obj(
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

        val response = ws.url(EVENT_URL).
          withHeaders(("Content-Type" -> "application/json"), ("Authorization", "Bearer " + tokenString)).
          withBody(jsObject).
          post()

        response
          .filter(res => res.status == CREATED)
          .flatMap(res => Future.successful(res))
      }
    }
  }

  override def add(obj: APIEvent, request: RequestHeader): Future[WSResponse] = ???

  override def update(obj: APIEvent, request: RequestHeader): Future[WSResponse] = ???
}
