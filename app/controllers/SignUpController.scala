package controllers

import javax.inject.Inject

import dao.PlainDAO
import forms.SignUpForm
import model.formaction.MailAddress
import model.user.RegisteredUser
import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{JsPath, Json}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.{Action, Controller}
import tokens.Token
import utils.{ConfigProvider, Mailer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * SignUp処理に関するActionが定義されています
  *
  * @param messagesApi 　多言語対応に必要
  */
class SignUpController @Inject()(val mailer: Mailer,
                                 aPIUserDAO: PlainDAO[RegisteredUser, WSResponse],
                                 cache: CacheApi,
                                 ws: WSClient,
                                 configProvider:ConfigProvider
                                )(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  // 仮登録メール送信画面の表示
  def signupStart = Action.async { implicit request =>
    Future.successful(Ok(views.html.no_secured.signup.signupstart(SignUpForm.registringDataForm)))
  }

  // 仮登録メールの送信確認画面の表示
  def signupMail = Action.async { implicit request =>
    val form = SignUpForm.registringDataForm
    form.bindFromRequest().fold(
      errorForm => Future.successful(Ok(views.html.errors.errorNotFound("mailCannotUse"))),
      signupData => {
        val mailAddress = signupData.mail
        val request: WSRequest = ws.
          url(configProvider.SIGNUP_MAIL_URL).
          withHeaders("Content-Type" -> "application/json")
        val jsonObject = Json.toJson(Map(
          "email" -> mailAddress
        ))
        request.post(jsonObject).flatMap {
          case res if res.status == OK => {
            Future.successful(Ok(views.html.no_secured.signup.signupmailsent()))
          }
          case res if res.status == BAD_REQUEST => Future.successful(Ok(views.html.errors.errorNotFound("mailCannotUse")))
        }
      }
    )
  }

  // アカウント本登録画面の表示
  def signup(token: String) = Action.async { implicit request =>
    val wsRequest: WSRequest = ws.
      url(configProvider.VERIFY_TOKEN_URL).withQueryString(("token", token))
    wsRequest.get().flatMap {
      case res if (res.status == OK) => {
        val body = res.body
        val email = Json.parse(body).validate((JsPath \ "email").read[String]).get
        Future.successful(Ok(views.html.no_secured.signup.signup(SignUpForm.registeredDataForm.bind(Map("mail" -> email)))))
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
        Future.successful(Ok(views.html.no_secured.signup.signupconfirm(form.fill(requestForm))))
      }
    )
  }

  // アカウント登録完了画面の表示
  def signupFinish = Action.async { implicit request =>
    SignUpForm.registeredDataForm.bindFromRequest().fold(
      errorForm => Future.successful(Redirect(routes.GuestHomeController.index())),
      requestForm => {
        aPIUserDAO.add(RegisteredUser(requestForm.id,
          Option(MailAddress(requestForm.mail)), requestForm.name, Option(requestForm.password._1), Option(requestForm.tel)), request).flatMap {
          case res => {
            val body = res.body
            val token = Json.parse(body)
            val tokenString = token.validate[Token].get.cookieString
            val jwtObject = JwtJson.decode(tokenString, configProvider.PUBLICKEY,Seq(JwtAlgorithm.RS512))
            val js = Json.parse(jwtObject.get.toJson)
            val id = (js \ "id").asOpt[String]
            cache.set(tokenString + ".userInfo", id.get)
            Future.successful(Ok(views.html.no_secured.signup.signupped()))
          }
        }
      }
    )
  }

}