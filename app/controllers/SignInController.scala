package controllers

import javax.inject.Inject

import dao.{AuthModule, Authenticator}
import forms.{CreateEventForm, SignInForm}
import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Cookie}
import tokens.Token
import utils.ConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SignInController @Inject()(val ws: WSClient,
                                 apiAuthenticator: Authenticator,
                                 cache: CacheApi,
                                configProvider:ConfigProvider
                                )(implicit val messagesApi: MessagesApi) extends AuthModule(cache,configProvider) with I18nSupport {

  // サインイン画面の表示
  def startSignin = Action.async { implicit request =>
    val optionCookie:Option[Cookie] = request.cookies.get(configProvider.COOKIE_NAME)
    optionCookie match {
      case Some(_) => Future.successful(Redirect(routes.RegisteredHomeController.index()))
      case None => Future.successful(Ok(views.html.no_secured.signin.signinstart(SignInForm.signInForm)))
    }
  }

  def signin = Action.async { implicit request =>
    SignInForm.signInForm.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.no_secured.signin.signinstart(SignInForm.signInForm))),
      signInData => {
        // IDとパスワードをAPIに送信し、トークンを取得
        apiAuthenticator.signin(signInData).flatMap {
          case response if response.status == OK => {
            val body:String = response.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            val jwtObject = JwtJson.decode(tokenString, configProvider.PUBLICKEY,Seq(JwtAlgorithm.RS512))
            val js = Json.parse(jwtObject.get.toJson)
            val name = (js \ "name").asOpt[String]
            cache.set(tokenString + ".userInfo", signInData.id)
            Future.successful(Ok(views.html.secured.index(CreateEventForm.createEventForm)(signInData.id)(name.get)).withHeaders((AUTHORIZATION, "Bearer " + tokenString)).withCookies(Cookie("evely_auth", tokenString)))
          }
          case response if response.status == BAD_REQUEST => {
            Future.successful(Ok(views.html.no_secured.signin.signinstart(SignInForm.signInForm)))
          }
          case e: Exception => Future.successful(Redirect(routes.SignInController.startSignin()))
        }
      })
  }
}
