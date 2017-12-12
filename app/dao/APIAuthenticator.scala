package dao

import javax.inject.Inject

import forms.{SignInData, SignInForm}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.Result

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class APIAuthenticator @Inject()(ws: WSClient) {
  def signup() = ???

  def signin(signInForm: Form[SignInData]) = {

    implicit val formatter = Json.format[SignInData]

    val request: WSRequest = ws.
      url("http://160.16.140.145:8888/api/develop/v1/auth/signin").
      withHeaders("Content-Type" -> "application/json")
    val jsonObject = Json.toJson(Map(
      "id" -> signInForm.get.userName,
      "password" -> signInForm.get.password
    ))
    val response = request.post(jsonObject)
    response.map{
      case res => res.body
      case _ => ""
    }
  }

  def signout() = ???
}
