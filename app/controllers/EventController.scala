package controllers

import javax.inject.Inject

import dao.{AuthModule, PlainDAO}
import forms.{CreateEventData, CreateEventForm, SearchEventForm}
import model.body.Body
import model.event.{Event, Location, UpcomingDate}
import model.user.RegisteredUser
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.mvc._
import utils.ConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EventController @Inject()(cache: CacheApi,
                                apiEventDAO: PlainDAO[CreateEventData, WSResponse],
                                configProvider: ConfigProvider)(implicit val messagesApi: MessagesApi) extends AuthModule(cache, configProvider) with I18nSupport {

  def create = withAuth { username =>
    implicit request =>
      CreateEventForm.createEventForm.bindFromRequest().fold(
        errorForm => {
          Future.successful(Redirect(routes.GuestHomeController.index()))
        },
        eventData => {
          apiEventDAO.save(eventData, request).map {
            case res => Redirect(routes.RegisteredHomeController.index())
            case _ => Redirect(routes.GuestHomeController.index())
          }
        }
      )
  }

  def search = Action.async { implicit request =>
    SearchEventForm.form.bindFromRequest().fold(
      error => Future.successful(Ok("error search")),
      form => {
        val query = Seq(("url", configProvider.EVENT_URL), ("keyword" -> form.keyword), ("limit" -> form.limit), ("offset" -> form.offset))
        val collection = apiEventDAO.find(query: _*) map {
          case res => {
            Json.parse(res.get.body).validate[List[Event]].get
          }
        }
//        val collection = List[Event](Event("1234",
//                                    "title2",
//  RegisteredUser("1234", Option("mail"), "kojima", Option("password"),Option("tel")),Body(Option("body")),Location("name", "", "lat", "lng"),Option("u-dapte"), List(UpcomingDate("","")),Option(""),Option.empty,Option.empty,Option.empty))
        collection.map(collection => Ok(views.html.search_view.search_map_layout(form.keyword)(collection)("t")("t")))
      }
    )
  }

  def findOffset = Action.async { implicit request =>
    SearchEventForm.form.bindFromRequest().fold(
      error => Future.successful(Ok("t")),
      form => {
        val query = Seq(("url", configProvider.EVENT_URL), ("keyword" -> form.keyword), ("limit" -> form.limit), ("offset" -> form.offset))
        val collection = apiEventDAO.find(query: _*) map {
          case res => {
            Json.parse(res.get.body).validate[List[Event]].get
          }
        }
        collection.map(collection => Ok(views.html.search_view.search_list_items(event => views.html.search_view.search_list_item_hr(event))(collection)))
      }
    )
  }

  def findOne(uid: String, eventId: String) = Action.async { implicit request =>
    val query = Seq(("url", configProvider.EVENT_URL + "/" + uid + "/" + eventId))

    apiEventDAO.find(query: _*) map {
      case res => {
        val event = Json.parse(res.get.body).validate[Event].get
        Ok(views.html.search_view.search_detail_item(event))
      }
    }
  }
}
