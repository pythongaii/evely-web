package controllers

import javax.inject.Inject

import forms.SignInForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Action
import play.api.mvc.Controller

import scala.concurrent.Future


class SignInController @Inject()(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport{

  // サインイン画面の表示
  def startSignin = Action.async { implicit request =>
    Future.successful(Ok(views.html.signin.signinstart(SignInForm.signInForm)))
  }

  // サインインの実行
  def signin = Action.async { implicit request =>
    Future.successful(Ok("execute signin"))
  }


}
