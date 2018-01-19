package dao

import javax.inject.Inject

import forms.CreateEventData
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.RequestHeader
import utils.ConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class APIEventDAO @Inject()(ws: WSClient, configProvider: ConfigProvider) extends PlainDAO[CreateEventData, WSResponse] {

  override def find(requestHeader: RequestHeader, key: (String, String)*): Future[Option[WSResponse]] = {

    val optionalCookie = requestHeader.cookies.get(configProvider.COOKIE_NAME)

    optionalCookie match {
      case Some(cookie) => {
        val tokenString = cookie.value
        val response = ws.url(key.head._2).
          withQueryString(key.tail: _*).
          withHeaders(("Authorization", "Bearer " + tokenString)).
          get()
        val f = Await.result(response, Duration.Inf)

        response
          .filter(res => res.status == OK)
          .flatMap(res => {
            res
            Future.successful(Option(res))
          })
      }
      case None => {
        val response = ws.url(key.head._2).
          withQueryString(key.tail: _*).
          get()

        response
          .filter(res => res.status == OK)
          .flatMap(res => Future.successful(Option(res)))
      }
    }
  }

  override def remove(request: RequestHeader, key: (String, String)*): Future[Unit] = ???

  def save(obj: CreateEventData, request: RequestHeader): Future[WSResponse] = {

    val optionalCookie = request.cookies.get(configProvider.COOKIE_NAME)

    optionalCookie match {
      case Some(cookie) => {
        val tokenString = cookie.value
        val jsObject = Json.obj(
          "body" -> obj.body,
          "mail" -> obj.mail,
          "noticeRange" -> obj.noticeRange,
          "openFlg" -> obj.openFlg,
          "schedules" ->

            obj.plans
          ,
          "scope" -> obj.scope,
          "tel" -> obj.tel,
          "title" -> obj.title,
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
