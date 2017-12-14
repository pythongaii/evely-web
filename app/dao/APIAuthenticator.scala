package dao

import javax.inject.Inject

import forms.{SignInData, SignInForm}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.Security

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Authen {
  def signup()
  def signin(signInData: SignInData): Future[WSResponse]
  def signout()
}

class APIAuthenticator @Inject()(ws: WSClient) extends Authen {
  def signup() = ???

  def signin(signInData: SignInData) = {

    implicit val formatter = Json.format[SignInData]

    val request: WSRequest = ws.
      url("http://160.16.140.145:8888/api/develop/v1/auth/signin").
      withHeaders("Content-Type" -> "application/json")
    val jsonObject = Json.toJson(Map(
      "id" -> signInData.id,
      "password" -> signInData.password
    ))
    request.post(jsonObject)
  }

  def signout() = ???
}
