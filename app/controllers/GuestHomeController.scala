package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class GuestHomeController @Inject()(ws: WSClient)(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index() = Action.async {
    Future.successful(Ok(views.html.top(List("event1", "event2", "event3", "event4"))))
  }

}
