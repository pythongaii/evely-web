package controllers

import javax.inject.Inject

import dao.PlainDAO
import forms.{CreateEventData, CreateEventForm, SearchEventForm}
import model.event.Event
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.mvc._
import utils.{AuthModule, ConfigProvider}

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
          eventData
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
        val collection = apiEventDAO.find(request,query: _*) map {
          case res => {
            Json.parse(res.get.body).validate[List[Event]].get
          }
        }
        request.cookies.get(configProvider.COOKIE_NAME) match {
          case None => collection.map(collection => Ok(views.html.search_view.search_map_layout(form.keyword)(collection)("")("")))
          case Some(_) => collection.map(collection => Ok(views.html.search_view.search_map_layout(form.keyword)(collection)("t")("t")))
        }
      }
    )
  }

  def findOffset = Action.async { implicit request =>
    SearchEventForm.form.bindFromRequest().fold(
      error => Future.successful(Ok("t")),
      form => {
        val query = Seq(("url", configProvider.EVENT_URL), ("keyword" -> form.keyword), ("limit" -> form.limit), ("offset" -> form.offset))
        val collection = apiEventDAO.find(request,query: _*) map {
          case res => {
            Json.parse(res.get.body).validate[List[Event]].get
          }
        }
        collection.map(collection => Ok(views.html.search_view.search_list_items(event => views.html.search_view.search_list_item_hr(event))(collection)))
      }
    )
  }

  def findOne(eventId: String) = Action.async { implicit request =>
    val query = Seq(("url", configProvider.EVENT_URL + "/detail"), ("ids", eventId))
    apiEventDAO.find(request,query: _*) map {
      case res => {
        val event = Json.parse(res.get.body).validate[Event].get
        Ok(views.html.search_view.search_detail_item(event))
      }
    }
  }

  def findOneGridSearch(eventId: String) = Action.async { implicit request =>
    val query = Seq(("url", configProvider.EVENT_URL + "/detail"), ("ids", eventId))
    apiEventDAO.find(request,query: _*) map {
      case res => {
        val events = Json.parse(res.get.body).validate[List[Event]].get
        Ok(views.html.search_view.grid_search_detail(events.head))
      }
    }
  }

  def fetchCreatingEvents = withAuth { username => implicit request =>
        val query = Seq(("url", configProvider.EVENT_URL + "/my_list"), ("limit" -> "10"), ("offset" -> "0"))
        val collection = apiEventDAO.find(request,query: _*) map {
          case res => {
            Json.parse(res.get.body).validate[List[Event]].get
          }
        }
        request.cookies.get(configProvider.COOKIE_NAME) match {
          case None => collection.map(collection => Ok(views.html.event_management.editting_events(collection)(username)("")))
          case Some(_) => collection.map(collection => Ok(views.html.event_management.editting_events(collection)(username)("t")))
        }
  }

  def editEvent(eventId:String) = Action.async { implicit request =>
    val query = Seq(("url", configProvider.EVENT_URL + "/detail"), ("ids", eventId))
    apiEventDAO.find(request,query: _*) map {
      case res => {
        val events = Json.parse(res.get.body).validate[List[Event]].get
        Ok(views.html.event_management.edit_event(events.head)("test"))
      }
    }
  }

  def updateEvent = withAuth { username =>
    implicit request =>
      CreateEventForm.createEventForm.bindFromRequest().fold(
        errorForm => {
          Future.successful(Redirect(routes.GuestHomeController.index()))
        },
        eventData => {
          eventData
          apiEventDAO.update(eventData, request).map {
            case res => Redirect(routes.RegisteredHomeController.index())
            case _ => Redirect(routes.GuestHomeController.index())
          }
        }
      )
  }
}
