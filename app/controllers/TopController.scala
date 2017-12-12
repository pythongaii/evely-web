package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import modules.CookieEnv
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.mvc._


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

//
class TopController @Inject()(ws: WSClient, silhouette: Silhouette[CookieEnv])(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

//  val eventList = {
//    val request: WSRequest = ws.url("http://160.16.140.145:8888/api/v1/events?limit=10&offset=0")
//    val futureres = request.get()
//    val events = futureres.map{
//      case res => res.body
//    }
//
//    Json.parse(events.value.get.get)
//  }

  def index() = Action.async {
    Future.successful(Ok(views.html.top(List("event1", "event2", "event3", "event4"))))
  }

  def secured = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.loginHome(List("event1", "event2", "event3", "event4"))))
  }

  def signout = silhouette.SecuredAction.async { implicit request =>
    silhouette.env.authenticatorService.discard(request.authenticator, Redirect(routes.TopController.index()))
  }
}
