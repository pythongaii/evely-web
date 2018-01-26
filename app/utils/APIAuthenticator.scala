package utils

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

class APIAuthenticator @Inject()(ws: WSClient, configProvider: ConfigProvider) extends Authenticator {

  def signup() = ???

  def signin(signInData: SignInData) = {

    implicit val formatter = Json.format[SignInData]

    val jsonObject = Json.toJson(Map(
      "id" -> signInData.id,
      "password" -> signInData.password
    ))

    ws.url(configProvider.SIGNIN_URL).
      withHeaders("Content-Type" -> "application/json")
      .post(jsonObject)
  }

  def signout() = ???
}
