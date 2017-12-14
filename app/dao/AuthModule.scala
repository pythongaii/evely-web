package dao

import javax.inject.Inject

import controllers.routes
import play.api.cache.{Cache, CacheApi}
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.mvc.Security

import scala.concurrent.Future
import scala.concurrent.duration.Duration

/**
  * Web側だけで認証を済ませる用のクラス
  * @param cache キャッシュに保存したtokenを
  */
class AuthModule @Inject()(val cache: CacheApi) extends Controller {

  /**
    * トークン保存に使ったクッキー名
    */
  val COOKIE_NAME = "evely_auth"

  def username(request: RequestHeader) = {
    val optionCookie:Option[Cookie] = request.cookies.get(COOKIE_NAME)
    optionCookie match {
      case None => Option.empty[String]
      case Some(cookie) => {
        val userToken = cookie.value
        val userID = cache.get[String](userToken + ".userInfo")
        userID match {
          case None => Option.empty[String]
          case Some(userID) => {
            cache.set(userToken + ".userInfo", userID, Duration.apply(60 * 30, "min"))
            Option(userID)
          }
        }
      }
    }
  }

  def removeAuthInfo(request: RequestHeader) = {
    val optionCookie = request.cookies.get(COOKIE_NAME)
    optionCookie match {
      case None => UNAUTHORIZED
      case Some(cookie) => cache.remove(cookie.value)
    }
  }

  def onUnauthorized(request: RequestHeader) = {
    Redirect(routes.SignInController.startSignin()).discardingCookies(DiscardingCookie(COOKIE_NAME))
  }

  def withAuth(f: => String => Request[AnyContent] => Future[Result]) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action.async(request => f(user)(request))
    }
  }
}
