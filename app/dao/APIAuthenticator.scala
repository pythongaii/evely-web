package dao

import javax.inject.Inject

import forms.SignInData
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future

trait Authenticator {
  def signup()
  def signin(signInData: SignInData): Future[WSResponse]
  def signout()
}

class APIAuthenticator @Inject()(ws: WSClient) extends Authenticator {

  def signup() = ???

  def signin(signInData: SignInData) = {

    implicit val formatter = Json.format[SignInData]

    val jsonObject = Json.toJson(Map(
      "id" -> signInData.id,
      "password" -> signInData.password
    ))

    ws.url("http://160.16.140.145:8888/api/develop/v1/auth/signin").
      withHeaders("Content-Type" -> "application/json")
      .post(jsonObject)
  }

  def signout() = ???
}
