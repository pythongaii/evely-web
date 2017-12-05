package controllers

import javax.inject.Inject

import forms.SignUpForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Action
import play.api.mvc.Controller

import scala.concurrent.Future

/**
  * SignUp処理に関するActionが定義されています
  * @param messagesApi　多言語対応に必要
  */
class SignUpController @Inject()(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // 仮登録メール送信画面の表示
  def startSignup() = Action.async {
    val form = SignUpForm.registringDataForm
    Future.successful(Ok(views.html.signup.signupstart(form)))
  }

  // 仮登録メールの送信確認画面の表示
  def signupmail = Action.async {
    Future.successful(Ok(views.html.signup.signupmailsent()))
  }

  // アカウント本登録画面の表示
  def signup = Action.async {
    Future.successful(Ok(views.html.signup.signup(SignUpForm.registerdDataForm)))
  }

  // アカウント登録、入力情報確認画面の表示
  def signupConfirm = Action.async { implicit request =>
    val form = SignUpForm.registerdDataForm
    form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok("error"))
      },
      requestForm => {
        Future.successful(Ok(views.html.signup.signupconfirm(form.fill(requestForm))))
      }
    )
  }

  // アカウント登録完了画面の表示
  def signupped = Action.async { implicit request =>
    Future.successful(Ok(views.html.signup.signupped()))
  }

}
