package controllers

import javax.inject.Inject

import dao.Authen
import forms.{CreateEventForm, SignInForm}
import model.Token
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Cookie}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import dao.AuthModule
import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.cache.CacheApi

class SignInController @Inject()(val ws: WSClient,
                                 apiAuthenticator: Authen,
                                 cache: CacheApi
                                )(implicit val messagesApi: MessagesApi) extends AuthModule(cache) with I18nSupport {

  // サインイン画面の表示
  def startSignin = Action.async { implicit request =>
    val optionCookie:Option[Cookie] = request.cookies.get(COOKIE_NAME)
    optionCookie match {
      case None => Future.successful(Ok(views.html.signin.signinstart(SignInForm.signInForm)))
      case Some(cookie) => Future.successful(Redirect(routes.RegisteredHomeController.index()))
    }
  }

  def signin = Action.async { implicit request =>
    SignInForm.signInForm.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.signin.signinstart(SignInForm.signInForm))),
      signinData => {
        // IDとパスワードをAPIに送信し、トークンを取得
        apiAuthenticator.signin(signinData).flatMap {
          case response if response.status == OK => {
            val body:String = response.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            val jwtObject = JwtJson.decode(tokenString, "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAp+E3V7ASC+8oyrsRoK9H\nly1hvLQnvV+hpDfSp4FDMkeIoBSrytBBlYc+p1gCq/KqMhhUTERWKYNAqh1nvyts\n0FKC8qKV+NfEDaig4BgA8fhMKg2CeWqy+JOURy/cFYZnitDS1JnDIvHvgN6YZfeG\nE1mPrB9g4M82OUyaxO2ejqZZwGcDvk1jSd3z+gGYErBvgXZ7GmnsQzeRZOb3Fn+U\nGBT84m/PXwpCKPDWHw5bKWPzuUfKT6lCtVNO1Crc5FF5v3XaHu7dExNzQTWRGByD\nwxP3k0hvrmM4czbuDbuqlqHPiUer/NZh0wiRE4mpQZZTmDfmudM/8rQ+1tqqSJ4l\nI6EosL+RodPUyLLjqWdI/OjJGK0kRcI8LLlFXlJVcQfYwBEErOzw0vhFLBVmNcM/\nUsVB59+o31oUtho36HiCOwXkqKrolT9PszfzQ3HQl/vLvQYSI1F3QPXt6TdgchSD\nvofpAkb3BVPRTw7Pz66Oqt43+lx53MWifw+R+fRgfjBcFvLENW3tfV6jQSMgG7z1\n1U9nRxIR9jPiuomLyWwg6oQsLzEtC4mw06RF2i7K8998N5IUzkVB1z4Du9REPUFk\nTcFqH3Qr74hb7mUL87gkP8FIVOEVW19h9gfKxfxUGPN5AJgr1t8Ap6HAX77BzqKR\ndE6qG4Y2olw3sf10La6yV/UCAwEAAQ==",Seq(JwtAlgorithm.RS512))
            val js = Json.parse(jwtObject.get.toJson)
            val name = (js \ "name").asOpt[String]
            cache.set(tokenString + ".userInfo", signinData.id)
            Future.successful(Ok(views.html.test.index(CreateEventForm.createEventForm)(signinData.id)(name.get)).withHeaders((AUTHORIZATION, "Bearer " + tokenString)).withCookies(Cookie("evely_auth", tokenString)))
          }
          case res if res.status == BAD_REQUEST => {
            Future.successful(Ok(views.html.signin.signinstart(SignInForm.signInForm)))
          }
          case e: Exception => Future.successful(Redirect(routes.SignInController.startSignin()))
        }
      })
  }
}
