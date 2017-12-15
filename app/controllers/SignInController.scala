package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.{APIAuthenticator, Authen}
import forms.{CreateEventForm, SignInData, SignInForm}
import model.Token
import org.joda.time.DateTime
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller, Cookie}
import service.{PasswordInfoService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import dao.AuthModule
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
      errorForm => Future.successful(Ok("error")),
      signinData => {
        // IDとパスワードをAPIに送信し、トークンを取得
        apiAuthenticator.signin(signinData).flatMap {
          case res => {
            val body = res.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            cache.set(tokenString + ".userInfo", "yKicchan")
            Future.successful(Ok(views.html.test.index(CreateEventForm.createEventForm)).withHeaders((AUTHORIZATION, "Bearer " + tokenString)).withCookies(Cookie("evely_auth", tokenString)))
          }
          case e: Exception => Future.successful(Redirect(routes.SignInController.startSignin()).flashing("error" -> "error"))
        }
      })
  }

  def secured = withAuth { username => implicit request =>
    Future.successful(Ok("fds"))
  }


  // サインインの実行
  //  def signin = Action.async { implicit request =>
  //    SignInForm.signInForm.bindFromRequest.fold(
  //      bagsForm => Future.successful(Ok("error")),
  //      signinData => {
  //        val credentials = Credentials(signinData.id, signinData.password)
  //        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
  //          userService.retrieve(loginInfo).flatMap {
  //            case None =>
  //              Future.successful(Redirect(routes.SignUpController.signupStart()).flashing("error" -> "会員登録を行ってください"))
  //            case Some(_) => for {
  //              authenticator <- silhouette.env.authenticatorService.create(loginInfo).map {
  //                case authenticator => authenticator
  //              }
  //              value <- silhouette.env.authenticatorService.init(authenticator)
  //              result <- silhouette.env.authenticatorService.embed(value, Redirect(routes.GuestHomeController.secured()))
  //            } yield result
  //          }
  //        }.recover {
  //          case e: ProviderException => Redirect(routes.SignUpController.signupStart()).flashing("error" -> "会員登録を行ってください")
  //        }
  //      })
  //  }

//  def apisignin = Action.async {
//    implicit request =>
//      val api: APIAuthenticator = new APIAuthenticator(ws)
//      val res = api.signin(SignInForm.signInForm.fill(SignInData("yKicchan", "password")))
//      res.map {
//        case respo => Ok(respo)
//        case _ => Ok("")
//      }
//  }
}
