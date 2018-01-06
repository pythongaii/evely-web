package dao

import javax.inject.Inject

import forms.CreateEventData
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.RequestHeader
import utils.ConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class APIEventDAO @Inject()(ws: WSClient, configProvider: ConfigProvider) extends PlainDAO[CreateEventData, WSResponse] {

  override def find(key: String): Future[Option[WSResponse]] = {
    val re = ws.url(configProvider.EVENT_URL).
      withQueryString(("keyword", key),("limit", "5"), ("offset", "0"))

    val response = re.get()

    response
      .filter(res => res.status == OK)
      .flatMap(res => Future.successful(Option(res)))
  }

  override def remove(key: String, request: RequestHeader): Future[Unit] = ???

  override def save(obj: CreateEventData, request: RequestHeader): Future[WSResponse] = {

    val optionalCookie = request.cookies.get(configProvider.COOKIE_NAME)

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

        val response = ws.url(configProvider.EVENT_URL).
          withHeaders(("Content-Type" -> "application/json"), ("Authorization", "Bearer " + tokenString)).
          post(jsObject)

        response
          .filter(res => res.status == CREATED)
          .flatMap(res => Future.successful(res))
      }
    }
  }

  override def add(obj: CreateEventData, request: RequestHeader): Future[WSResponse] = ???

  override def update(obj: CreateEventData, request: RequestHeader): Future[WSResponse] = ???
}
