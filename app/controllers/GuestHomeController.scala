package controllers

import javax.inject.Inject

import dao.PlainDAO
import forms.CreateEventData
import model.event.Event
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{Action, Controller}
import utils.ConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class GuestHomeController @Inject()(apiEventDAO: PlainDAO[CreateEventData, WSResponse],configProvider: ConfigProvider)(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index() = Action.async { implicit request =>
    val query = Seq(("url", configProvider.EVENT_URL), ("keyword" -> "Git"), ("limit" -> "10"), ("offset" -> "0"))
    val collection = apiEventDAO.find(request,query: _*) map {
      case res => {
        Json.parse(res.get.body).validate[List[Event]].get
      }
    }
    collection.map(collection => Ok(views.html.guest_home(collection)))
  }

}
