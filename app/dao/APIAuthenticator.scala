package dao

import javax.inject.Inject

import forms.SignInForm
import play.api.data.Form
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.Result

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class APIAuthenticator @Inject()(ws: WSClient){
  def signup() = ???
  def signin(signInForm: Form[SignInForm.SignInData]):Future[String] = {

    val request: WSRequest = ws.url("http://160.16.140.145:8888/api/v1/auth/signin")
    val response = request.post(Map("userID" -> Seq("abc"), "password" -> Seq("123")))
  response.map{
    case res => res.body
  }
  }
  def signout() = ???
}
