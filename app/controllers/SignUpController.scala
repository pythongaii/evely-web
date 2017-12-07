package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.SignUpForm
import model.formaction.MailAddress
import model.user.RegisteredUser
import modules.CookieEnv
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Action
import play.api.mvc.Controller
import service.{PasswordInfoService, SignUpTokenService, UserService}
import utils.Mailer

import scala.concurrent.Future

/**
  * SignUp処理に関するActionが定義されています
  * @param messagesApi　多言語対応に必要
  */
class SignUpController @Inject()(val silhouette: Silhouette[CookieEnv],
                                 userService: UserService,
                                 passwordInfoService: PasswordInfoService,
                                 passwordHasher: PasswordHasher,
                                 signUpTokenService: SignUpTokenService,
                                 mailer: Mailer
                                )(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // 仮登録メール送信画面の表示
  def signupStart = silhouette.UserAwareAction.async { implicit request =>
    Future.successful(request.identity match {
      case Some(user) => Redirect(routes.TopController.index())
      case None => Ok(views.html.signup.signupstart(SignUpForm.registringDataForm))
    })
  }

  // 仮登録メールの送信確認画面の表示
  def signupMail = Action.async { implicit request =>
    SignUpForm.registringDataForm.bindFromRequest.fold(
      bogusForm => Future.successful(BadRequest(views.html.signup.startsignup())),
      signUpData => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, signUpData.mailaddress)
        userService.retrieve(loginInfo).flatMap {
          case Some(_) =>
            Future.successful(Redirect(routes.SignUpController.startsignup()).flashing(
              "error" -> "error"))
          case None =>
            val registeredUser = RegisteredUser(
              userName = "",
              mailAddress = MailAddress(signUpData.mailaddress),
              realName = "",
              tel = Option.empty,
              loginInfo = loginInfo
            )
            for {
//              avatarUrl <- avatarService.retrieveURL(signUpData.email)
              user <- userService.save(registeredUser)
              token <- signUpTokenService.save(signUpTokenService.createUserToken(signUpData.mailaddress, false))
            } yield {
              mailer.confirm(signUpData.mailaddress, link = routes.SignUpController.signup(token.tokenID.toString).absoluteURL())
              Ok(views.html.signup.signupmailsent())
            }
        }
      }
    )
  }

  // アカウント本登録画面の表示
  def signup(tokenID: String) = Action.async {
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
  def signupFinish = Action.async { implicit request =>
    Future.successful(Ok(views.html.signup.signupped()))
  }

}
