package controllers

import javax.inject.Inject

import dao.PlainDAO
import forms.{CreateEventData, CreateEventForm, SignInForm}
import model.event.Event
import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{Action, Cookie}
import tokens.Token
import utils.{AuthModule, Authenticator, ConfigProvider}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SignInController @Inject()(val ws: WSClient,
                                 apiAuthenticator: Authenticator,
                                 cache: CacheApi,
                                 configProvider: ConfigProvider,
                                 apiEventDAO: PlainDAO[CreateEventData, WSResponse]
                                )(implicit val messagesApi: MessagesApi) extends AuthModule(cache, configProvider) with I18nSupport {

  // サインイン画面の表示
  def startSignin = Action.async { implicit request =>
    val optionCookie: Option[Cookie] = request.cookies.get(configProvider.COOKIE_NAME)
    optionCookie match {
      case None => Future.successful(Ok(views.html.signin.signinstart()))
      case Some(_) => Future.successful(Redirect(routes.RegisteredHomeController.index()))
    }
  }

  def signin = Action.async { implicit request =>
    SignInForm.signInForm.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.signin.signinstart())),
      signInData => {
        // IDとパスワードをAPIに送信し、トークンを取得
        apiAuthenticator.signin(signInData).flatMap {
          case response if response.status == OK => {
            val body: String = response.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            val jwtObject = JwtJson.decode(tokenString, configProvider.PUBLICKEY, Seq(JwtAlgorithm.RS512))
            val js = Json.parse(jwtObject.get.toJson)
            val id = (js \ "id").asOpt[String]
            cache.set(tokenString + ".userInfo", signInData.id)
            val query = Seq(("url", configProvider.EVENT_URL), ("keyword" -> "Git"), ("limit" -> "9"), ("offset" -> "0"))
            val collection = apiEventDAO.find(request,query: _*) map {
              case res => {
                Json.parse(res.get.body).validate[List[Event]].get
              }
            }
            collection.map(collection => Ok(views.html.secured.index(collection)(signInData.id)).withHeaders((AUTHORIZATION, "Bearer " + tokenString)).withCookies(Cookie("evely_auth", tokenString)))
          }
          case response if response.status == BAD_REQUEST => {
            Future.successful(Ok(views.html.signin.signinstart()))
          }
          case e: Exception => Future.successful(Redirect(routes.SignInController.startSignin()))
        }
      })
  }
}
