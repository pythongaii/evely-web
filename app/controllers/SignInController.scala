package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.APIAuthenticator
import forms.{SignInData, SignInForm}
import modules.CookieEnv
import org.joda.time.DateTime
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.mvc.Action
import play.api.mvc.Controller
import service.{PasswordInfoService, UserService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration




class SignInController @Inject()(val silhouette: Silhouette[CookieEnv],
                                 userService: UserService,
                                 passwordInfoService: PasswordInfoService,
                                 credentialsProvider: CredentialsProvider,
                                 ws: WSClient
                                )(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // サインイン画面の表示
  def startSignin = silhouette.UserAwareAction.async { implicit request =>
    Future.successful(request.identity match {
      case Some(_) => Redirect(routes.TopController.index())
      case None => Ok(views.html.signin.signinstart(SignInForm.signInForm))
    })
  }

  // サインインの実行
  def signin = Action.async { implicit request =>
    SignInForm.signInForm.bindFromRequest.fold(
      bagsForm => Future.successful(Ok("error")),
      signinData => {
        val credentials = Credentials(signinData.userName, signinData.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {
            case None =>
              Future.successful(Redirect(routes.SignUpController.signupStart()).flashing("error" -> "会員登録を行ってください"))
            case Some(_) => for {
              authenticator <- silhouette.env.authenticatorService.create(loginInfo).map {
                case authenticator => authenticator
              }
              value <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(value, Redirect(routes.TopController.secured()))
            } yield result
          }
        }.recover {
          case e: ProviderException => Redirect(routes.SignUpController.signupStart()).flashing("error" -> "会員登録を行ってください")
        }
      })
  }

  def apisignin = Action.async { implicit request =>
    val api : APIAuthenticator = new APIAuthenticator(ws)
    val res = api.signin(SignInForm.signInForm.fill(SignInData("yKicchan", "password")))
    res.map{
      case respo => Ok(respo)
      case _ => Ok("")
    }
  }
}
