package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import modules.CookieEnv
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class GuestHomeController @Inject()(ws: WSClient, silhouette: Silhouette[CookieEnv])(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index() = Action.async {
    Future.successful(Ok(views.html.top(List("event1", "event2", "event3", "event4"))).withHeaders(("Authorization", "bearear tokens")))
  }

  def secured = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.loginHome(List("event1", "event2", "event3", "event4"))))
  }

  def signout = silhouette.SecuredAction.async { implicit request =>
    silhouette.env.authenticatorService.discard(request.authenticator, Redirect(routes.GuestHomeController.index()))
  }


}
