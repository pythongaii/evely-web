package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Action
import play.api.mvc.Controller

import scala.concurrent.Future

class ForgotPasswordController @Inject()(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // パスワードの再設定メール送信用画面の表示
  def startForgotPass = Action.async{ implicit request =>
    Future.successful(Ok(views.html.forgotpass.forgotpassstart()))
  }

  // パスワード再設定メールの送信
  def forgotpassMail = Action.async { implicit request =>
    Future.successful(Ok(views.html.forgotpass.forgotpassmailsent()))
  }

  // パスワードの再設定画面の表示
  def forgotpassreset = Action.async {implicit request =>
    Future.successful(Ok(views.html.forgotpass.forgotpassreset(forms.ForgotPassForm.resetForm)))
  }

  // パスワード再設定の完了
  def forgotpass = Action.async { implicit request =>
    Future.successful(Ok(views.html.forgotpass.forgotpass()))
  }

}