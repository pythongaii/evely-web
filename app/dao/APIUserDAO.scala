package dao

import javax.inject.Inject

import model.user.RegisteredUser
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.http.Status._
import play.api.mvc.RequestHeader

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class APIUserDAO @Inject()(ws: WSClient) extends PlainDAO[String, RegisteredUser, WSResponse] {

  override def add(obj: RegisteredUser, request: RequestHeader): Future[WSResponse] = {
    val jso = Json.obj(
      "id" -> obj.id,
      "mail" -> obj.mail.mailAddress,
      "name" -> obj.name,
      "password" -> obj.password,
      "tel" -> obj.tel
    )

    val response = ws.url("http://160.16.140.145:8888/api/develop/v1/auth/signup").
      withHeaders(("Content-Type" -> "application/json")).
      withMethod("POST").
      withBody(jso).
      execute()

    response.flatMap {
      case res => {
        Future.successful(res)
      }
    }
  }


  override def find(key: String): Future[Option[WSResponse]] = ???

  override def save(obj: RegisteredUser, request: RequestHeader): Future[WSResponse] = ???

  override def remove(key: String): Future[Unit] = ???

  override def update(obj: RegisteredUser, request: RequestHeader): Future[WSResponse] = ???

  //  override def add(user: RegisteredUser): Future[RegisteredUser] = ???
  //
  //  override def find(userName: String): Future[Option[RegisteredUser]] = ???
  //
  //  override def find(loginInfo: LoginInfo): Future[Option[RegisteredUser]] = ???
  //
  //  override def remove(loginInfo: LoginInfo): Future[Unit] = ???
  //
  //  override def remove(userName: String): Future[Unit] = ???
  //
  //  override def save(user: RegisteredUser): Future[RegisteredUser] = ???
  //
  //  override def update(user: RegisteredUser): Future[RegisteredUser] = ???

}
