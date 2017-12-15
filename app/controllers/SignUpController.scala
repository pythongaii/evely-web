package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.{APIUserDAO, PlainDAO}
import forms.{CreateEventForm, SignUpForm}
import model.Token
import model.formaction.MailAddress
import model.user.RegisteredUser
import play.api.cache
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.mvc.{Action, Controller, Cookie}
import play.cache.Cache
import service.{PasswordInfoService, SignUpTokenService}
import utils.Mailer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * SignUp処理に関するActionが定義されています
  *
  * @param messagesApi 　多言語対応に必要
  */
class SignUpController @Inject()(val
                                 passwordInfoService: PasswordInfoService,
                                 passwordHasher: PasswordHasher,
                                 signUpTokenService: SignUpTokenService,
                                 mailer: Mailer,
                                 aPIUserDAO: PlainDAO[String, RegisteredUser, WSResponse],
                                 cache: CacheApi
                                )(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // 仮登録メール送信画面の表示
  def signupStart = Action.async { implicit request =>
    Future.successful(Redirect(routes.SignUpController.signup()))
    //    Future.successful(request.identity match {
    //      case Some(user) => Redirect(routes.GuestHomeController.index())
    //      case None => Ok(views.html.signup.signupstart(SignUpForm.registringDataForm))
    //    })
  }

  // 仮登録メールの送信確認画面の表示
  def signupMail = Action.async { implicit request =>
    Future.successful(Ok(" d"))
    //    SignUpForm.registringDataForm.bindFromRequest.fold(
    //      bogusForm => Future.successful(Ok("")),
    //      signUpData => {
    //        val loginInfo = LoginInfo(CredentialsProvider.ID, signUpData.mailaddress)
    //        userService.retrieve(loginInfo).flatMap {
    //          case Some(_) =>
    //            Future.successful(Redirect(routes.SignUpController.signupStart()).flashing(
    //              "error" -> "error"))
    //          case None =>
    //            val registeredUser = RegisteredUser(
    //              userName = "",
    //              mailAddress = MailAddress(signUpData.mailaddress),
    //              realName = "",
    //              tel = Option.empty,
    //              loginInfo = loginInfo,
    //              mailConfirmed = false
    //            )
    //            for {
    ////              avatarUrl <- avatarService.retrieveURL(signUpData.email)
    //              user <- userService.save(registeredUser)
    //              token <- signUpTokenService.save(signUpTokenService.createUserToken(signUpData.mailaddress, false))
    //            } yield {
    //              mailer.confirm(signUpData.mailaddress, link = routes.SignUpController.signup(token.tokenID.toString).absoluteURL())
    //              Ok(views.html.signup.signupmailsent())
    //            }
    //        }
    //      }
    //    )
  }

  // アカウント本登録画面の表示
//  def signup(tokenID: String) = Action.async {
//    val form = SignUpForm.registeredDataForm
//    Future.successful(Ok(views.html.signup.signup(form)))
//  }

  def signup() = Action.async {
    val form = SignUpForm.registeredDataForm
    Future.successful(Ok(views.html.signup.signup(form)))
  }

  // アカウント登録、入力情報確認画面の表示
  def signupConfirm = Action.async { implicit request =>
    val form = SignUpForm.registeredDataForm
    form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Redirect(routes.GuestHomeController.index()))
      },
      requestForm => {
        Future.successful(Ok(views.html.signup.signupconfirm(form.fill(requestForm))))
      }
    )
  }

  // アカウント登録完了画面の表示
  def signupFinish = Action.async { implicit request =>
    SignUpForm.registeredDataForm.bindFromRequest().fold(
      errorForm => Future.successful(Ok("te")),
      requestForm => {
        aPIUserDAO.add(RegisteredUser(requestForm.id,
          MailAddress(requestForm.mail), requestForm.name, requestForm.password._1, requestForm.tel), request).flatMap {
          case res => {
            val body = res.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            cache.set(tokenString + ".userInfo", "yKicchan")
            Future.successful(Ok(views.html.signup.signupped()))
          }
        }
      }
    )
    ////    val form = SignUpForm.registerdDataForm
    ////    form.bindFromRequest().fold(
    ////      errorForm => {
    ////        Future.successful(Ok("error"))
    ////      },
    ////      requestForm => {
    ////        signUpTokenService.find(UUID.fromString("")).flatMap {
    ////          case None =>
    ////            Future.successful(Ok("token not found"))
    ////          case Some(token) if !token.isSignUp && !token.isExpired =>
    ////            userService.find(token.mailAddress).flatMap {
    ////              case None => Future.failed(new IdentityNotFoundException(Messages("error.noUser")))
    ////              case Some(user) =>
    ////                val loginInfo = LoginInfo(CredentialsProvider.ID, token.mailAddress)
    ////                val registeredUser = RegisteredUser(
    ////                   = requestForm.id,
    ////                  mailAddress = user.mailAddress,
    ////                  realName = requestForm.name,
    ////                  tel = Option.empty[String],
    ////                  loginInfo = loginInfo,
    ////                  mailConfirmed = true
    ////                )
    ////                for {
    ////                  authenticator <- silhouette.env.authenticatorService.create(loginInfo)
    ////                  value <- silhouette.env.authenticatorService.init(authenticator)
    ////                  _ <- userService.update(registeredUser)
    ////                  _ <- signUpTokenService.remove(UUID.fromString(""))
    ////                  _ <- passwordInfoService.save(loginInfo, passwordHasher.hash(requestForm.password._1))
    ////                  result <- silhouette.env.authenticatorService.embed(value, Redirect(routes.SignInController.secured()))
    ////                } yield result
    ////            }
    ////          case Some(token) =>
    ////            signUpTokenService.remove(UUID.fromString("")).map {_ => NotFound(views.html.errors.notFound(request))}
    ////        }
    ////      }
    //    )
  }
}