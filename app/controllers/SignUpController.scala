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
import play.api.libs.json.{JsPath, Json}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.{Action, Controller, Cookie}
import pdi.jwt.{JwtAlgorithm, JwtJson, JwtSession}
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
                                 cache: CacheApi,
                                 ws: WSClient
                                )(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // 仮登録メール送信画面の表示
  def signupStart = Action.async { implicit request =>
    Future.successful(Ok(views.html.signup.signupstart(SignUpForm.registringDataForm)))
  }

  // 仮登録メールの送信確認画面の表示
  def signupMail = Action.async { implicit request =>
    val form = SignUpForm.registringDataForm
    form.bindFromRequest().fold(
      errorForm => Future.successful(Ok(views.html.errors.errorNotFound("mailCannotUse"))),
      signupData => {
        val mailAddress = signupData.mailaddress
        val request: WSRequest = ws.
          url("http://160.16.140.145:8888/api/develop/v1/auth/signup/send_mail").
          withHeaders("Content-Type" -> "application/json")
        val jsonObject = Json.toJson(Map(
          "email" -> mailAddress
        ))
        request.post(jsonObject).flatMap {
          case res if res.status == OK => {
            Future.successful(Ok(views.html.signup.signupmailsent()))
          }
          case res if res.status == BAD_REQUEST => Future.successful(Ok(views.html.errors.errorNotFound("mailCannotUse")))
        }
      }
    )
  }

  // アカウント本登録画面の表示
  def signup(token: String) = Action.async { implicit request =>
    val wsRequest: WSRequest = ws.
      url("http://160.16.140.145:8888/api/develop/v1/auth/signup/verify_token").withQueryString(("token", token))
    wsRequest.get().flatMap {
      case res if (res.status == OK) => {
        val body = res.body
        val email = Json.parse(body).validate((JsPath \ "email").read[String]).get
        Future.successful(Ok(views.html.signup.signup(SignUpForm.registeredDataForm.bind(Map("mail" -> email)))))
      }
      case _ => Future.successful(Ok(views.html.errors.errorNotFound("mailCannotUse")))
    }

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
      errorForm => Future.successful(Redirect(routes.GuestHomeController.index())),
      requestForm => {
        aPIUserDAO.add(RegisteredUser(requestForm.id,
          MailAddress(requestForm.mail), requestForm.name, requestForm.password._1, requestForm.tel), request).flatMap {
          case res => {
            val body = res.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            val jwtObject = JwtJson.decode(tokenString, "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAp+E3V7ASC+8oyrsRoK9H\nly1hvLQnvV+hpDfSp4FDMkeIoBSrytBBlYc+p1gCq/KqMhhUTERWKYNAqh1nvyts\n0FKC8qKV+NfEDaig4BgA8fhMKg2CeWqy+JOURy/cFYZnitDS1JnDIvHvgN6YZfeG\nE1mPrB9g4M82OUyaxO2ejqZZwGcDvk1jSd3z+gGYErBvgXZ7GmnsQzeRZOb3Fn+U\nGBT84m/PXwpCKPDWHw5bKWPzuUfKT6lCtVNO1Crc5FF5v3XaHu7dExNzQTWRGByD\nwxP3k0hvrmM4czbuDbuqlqHPiUer/NZh0wiRE4mpQZZTmDfmudM/8rQ+1tqqSJ4l\nI6EosL+RodPUyLLjqWdI/OjJGK0kRcI8LLlFXlJVcQfYwBEErOzw0vhFLBVmNcM/\nUsVB59+o31oUtho36HiCOwXkqKrolT9PszfzQ3HQl/vLvQYSI1F3QPXt6TdgchSD\nvofpAkb3BVPRTw7Pz66Oqt43+lx53MWifw+R+fRgfjBcFvLENW3tfV6jQSMgG7z1\n1U9nRxIR9jPiuomLyWwg6oQsLzEtC4mw06RF2i7K8998N5IUzkVB1z4Du9REPUFk\nTcFqH3Qr74hb7mUL87gkP8FIVOEVW19h9gfKxfxUGPN5AJgr1t8Ap6HAX77BzqKR\ndE6qG4Y2olw3sf10La6yV/UCAwEAAQ==",Seq(JwtAlgorithm.RS512))
            val js = Json.parse(jwtObject.get.toJson)
            val id = (js \ "id").asOpt[String]
            cache.set(tokenString + ".userInfo", id.get)
            Future.successful(Ok(views.html.signup.signupped()))
          }
        }
      }
    )
  }
}