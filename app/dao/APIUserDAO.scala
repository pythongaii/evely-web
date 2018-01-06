package dao

import javax.inject.Inject

import model.user.RegisteredUser
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.RequestHeader
import utils.ConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class APIUserDAO @Inject()(ws: WSClient, configProvider: ConfigProvider) extends PlainDAO[RegisteredUser, WSResponse] {

  override def add(obj: RegisteredUser, request: RequestHeader): Future[WSResponse] = {

    val jsObject = Json.obj(
      "id" -> obj.id,
      "mail" -> obj.mail.get.mailAddress,
      "name" -> obj.name,
      "password" -> obj.password,
      "tel" -> obj.tel
    )

    val response = ws.url(configProvider.SIGNUP_URL).
      withHeaders(("Content-Type" -> "application/json")).
      post(jsObject)

    response.flatMap(res => Future.successful(res))
  }

  override def find(key: String): Future[Option[WSResponse]] = ???

  override def save(obj: RegisteredUser, request: RequestHeader): Future[WSResponse] = ???

  override def remove(key: String, request: RequestHeader): Future[Unit] = ???

  override def update(obj: RegisteredUser, request: RequestHeader): Future[WSResponse] = ???

}
